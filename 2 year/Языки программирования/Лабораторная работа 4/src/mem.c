#define _DEFAULT_SOURCE

#include <stdbool.h>
#include <stddef.h>
#include <sys/mman.h>

#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include "mem.h"
#include "mem_internals.h"
#include "util.h"

void debug_block(struct block_header* b, const char* fmt, ... );
void debug(const char* fmt, ... );

extern inline block_size size_from_capacity( block_capacity cap );
extern inline block_capacity capacity_from_size( block_size sz );

static bool            block_is_big_enough( size_t query, struct block_header* block ) { return block->capacity.bytes >= query; }
static size_t          pages_count   ( size_t mem )                      { return mem / sysconf(_SC_PAGESIZE) + ((mem % sysconf(_SC_PAGESIZE)) > 0); }
static size_t          round_pages   ( size_t mem )                      { return  sysconf(_SC_PAGESIZE)* pages_count( mem ) ; }

static void block_init( void* restrict addr, block_size block_sz, void* restrict next ) {
  *((struct block_header*)addr) = (struct block_header) {
    .next = next,
    .capacity = capacity_from_size(block_sz),
    .is_free = true
  };
}

static size_t region_actual_size( size_t query ) { return size_max( round_pages( query ), REGION_MIN_SIZE ); }

extern inline bool region_is_invalid( const struct region* r );



static void* map_pages(void const* addr, size_t length, int additional_flags) {
  return mmap( (void*) addr, length, PROT_READ | PROT_WRITE, MAP_PRIVATE | MAP_ANONYMOUS | additional_flags , -1, 0 );
}

/*  аллоцировать регион памяти и инициализировать его блоком */
static struct region alloc_region  ( void const * addr, size_t query ) {
  const size_t region_size = region_actual_size(query + offsetof(struct block_header, contents));
  void* mapped_addr = map_pages(addr, region_size, MAP_FIXED);
  bool extends = true;
  if (mapped_addr == MAP_FAILED){
    extends = false;
    mapped_addr = map_pages(addr, region_size, 0);
    if (mapped_addr == MAP_FAILED)
      return REGION_INVALID;
  }
  struct region new_region = {
    .addr = mapped_addr,
    .size = region_size,
    .extends = extends
  };
  block_init(mapped_addr, (block_size){.bytes = region_size}, NULL);
  return new_region;
}

static void* block_after( struct block_header const* block )         ;

void* heap_init( size_t initial ) {
  const struct region region = alloc_region( HEAP_START, initial );
  if ( region_is_invalid(&region) ) return NULL;

  return region.addr;
}



#define BLOCK_MIN_CAPACITY 24

/*  --- Разделение блоков (если найденный свободный блок слишком большой )--- */

static bool block_splittable( struct block_header* restrict block, size_t query) {
  return block-> is_free && query + offsetof( struct block_header, contents ) + BLOCK_MIN_CAPACITY <= block->capacity.bytes;
}

static bool split_if_too_big( struct block_header* block, size_t query ) {
  query = size_max(query, BLOCK_MIN_CAPACITY);
  if (!block || !block_splittable(block, query)) return false;
  void* second_block_ptr = block->contents + query;
  block_size second_block_size = (block_size) {
    .bytes = block->capacity.bytes - query
  };
  block_init(second_block_ptr, second_block_size, block->next);
  block->capacity.bytes = query;
  block->next = second_block_ptr;
  return true;
}


/*  --- Слияние соседних свободных блоков --- */

static void* block_after( struct block_header const* block )              {
  return  (void*) (block->contents + block->capacity.bytes);
}
static bool blocks_continuous (
                               struct block_header const* fst,
                               struct block_header const* snd ) {
  return (void*)snd == block_after(fst);
}

static bool mergeable(struct block_header const* restrict fst, struct block_header const* restrict snd) {
  return fst->is_free && snd->is_free && blocks_continuous( fst, snd ) ;
}

static bool try_merge_with_next( struct block_header* block ) {
  if (!block || !block->next || !mergeable(block, block->next)) return false;

  block->capacity.bytes += block->next->capacity.bytes + offsetof(struct block_header, contents);
  block->next = block->next->next;
  return true;
}


/*  --- ... ecли размера кучи хватает --- */

struct block_search_result {
  enum {BSR_FOUND_GOOD_BLOCK, BSR_REACHED_END_NOT_FOUND, BSR_CORRUPTED} type;
  struct block_header* block;
};


static struct block_search_result find_good_or_last  ( struct block_header* restrict block, size_t sz )    {
  if (!block) return (struct block_search_result){.type=BSR_CORRUPTED, .block=block};
  while(block->next != NULL){
    if (try_merge_with_next(block)) continue;
    if(block->is_free && block_is_big_enough(sz, block))
      return (struct block_search_result){.type=BSR_FOUND_GOOD_BLOCK, .block=block};
    block = block->next;
  }
  if(block->is_free && block_is_big_enough(sz, block))
    return (struct block_search_result){.type=BSR_FOUND_GOOD_BLOCK, .block=block};
  return (struct block_search_result){.type=BSR_REACHED_END_NOT_FOUND, .block=block};
}

/*  Попробовать выделить память в куче начиная с блока `block` не пытаясь расширить кучу
 Можно переиспользовать как только кучу расширили. */
static struct block_search_result try_memalloc_existing ( size_t query, struct block_header* block ) {
  if (!block) return (struct block_search_result) {.type = BSR_CORRUPTED, .block=NULL};
  query = size_max(query, BLOCK_MIN_CAPACITY);
  struct block_search_result block_search_result = find_good_or_last(block, query);
  if (block_search_result.type != BSR_FOUND_GOOD_BLOCK) 
    return block_search_result;
  split_if_too_big(block_search_result.block, query);
  block_search_result.block->is_free = false;
  return block_search_result;
}



static struct block_header* grow_heap( struct block_header* restrict last, size_t query ) {
  struct region new_region = alloc_region(block_after(last), query);
  if(new_region.addr == NULL)
    return NULL;
  last->next = new_region.addr;
  if (try_merge_with_next(last)){
    return last;
  }
  return last->next;
}


//
/*  Реализует основную логику malloc и возвращает заголовок выделенного блока */
static struct block_header* memalloc( size_t query, struct block_header* heap_start) {
  struct block_search_result result = try_memalloc_existing(query, heap_start);
  if (result.type == BSR_CORRUPTED)
    return NULL;
  if (result.type == BSR_FOUND_GOOD_BLOCK)
    return result.block;
  struct block_header* new_block = grow_heap(result.block, query);
  if (!new_block)
    return NULL;
  result = try_memalloc_existing(query, new_block);
  if (result.type == BSR_CORRUPTED)
    return NULL;
  if (result.type == BSR_FOUND_GOOD_BLOCK)
    return result.block;
  return NULL;
}

void* _malloc( size_t query ) {
  struct block_header* const addr = memalloc( query, (struct block_header*) HEAP_START );
  if (addr) return addr->contents;
  else return NULL;
}

static struct block_header* block_get_header(void* contents) {
  return (struct block_header*) (((uint8_t*)contents)-offsetof(struct block_header, contents));
}

void _free( void* mem ) {
  if (!mem) return;
  struct block_header* header = block_get_header( mem );
  header->is_free = true;
  while (try_merge_with_next(header));
}

/*  освободить всю память, выделенную под кучу */
void heap_term( ) {
  struct block_header* block_start = (struct block_header*)HEAP_START;
  struct block_header* block_cur = block_start;
  size_t sz = 0;
  while(block_cur){
    sz += size_from_capacity(block_cur->capacity).bytes;
    if (block_cur->next && blocks_continuous(block_cur, block_cur->next)){
      block_cur = block_cur->next;
      continue;
    }
    block_cur = block_cur->next;
    munmap(block_start, round_pages(sz));
    block_start = block_cur;
    sz = 0;
  }
}

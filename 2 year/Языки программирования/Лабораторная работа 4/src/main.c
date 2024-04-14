#include "mem.h"
#include "mem_internals.h"
#include <assert.h>
#include <stddef.h>
#include <stdint.h>
#include <stdio.h>
#include <sys/mman.h>

#define REGION_STD_SIZE 1000


static struct block_header* get_header(void *contents) {
  return (struct block_header *)(((uint8_t *)contents) -
                                 offsetof(struct block_header, contents));
}

static void print_test_start(const char *s){
  printf("Test: %s\n", s);
}

static void print_test_ended(){
  puts("Test: Passed");
}

void test_simple_allocation(){
  print_test_start("Simple allocation");
  int64_t* region = _malloc(REGION_MIN_SIZE);
  debug_heap(stderr, HEAP_START);
  assert(region);
  _free(region);
  print_test_ended();
} 

void test_free_one(){
  print_test_start("Test freeing one of blocks");
  int64_t* region = _malloc(REGION_STD_SIZE);
  int64_t* region2 = _malloc(10);
  int64_t* region3 = _malloc(10);
  assert(region);
  assert(region2);
  assert(region3);
  _free(region);
  debug_heap(stderr, HEAP_START);
  assert(get_header(region)->is_free && !get_header(region2)->is_free && !get_header(region3)->is_free);
  _free(region2);
  _free(region3);
  print_test_ended();
}

void test_free_all(){
  print_test_start("Test freeing more than one block");
  int64_t* region = _malloc(REGION_STD_SIZE);
  int64_t* region2 = _malloc(10);
  int64_t* region3 = _malloc(16);
  int64_t* region4 = _malloc(15);
  assert(region);
  assert(region2);
  assert(region3);
  _free(region);
  _free(region2);
  _free(region4);
  debug_heap(stderr, HEAP_START);
  assert(get_header(region)->is_free);
  assert(get_header(region2)->is_free); 
  assert(!get_header(region3)->is_free); 
  assert(get_header(region4)->is_free);
  _free(region3);
  print_test_ended();
}

void test_mmap_extend(){
  print_test_start("using mmap");
  puts("Before mmap:");
  debug_heap(stderr, HEAP_START);
  void* data = _malloc(REGION_STD_SIZE * 35);
  assert(data);
  puts("After first malloc:");
  debug_heap(stderr, HEAP_START);
  void* data2 = _malloc(REGION_STD_SIZE * 100);
  puts("After second malloc:");
  debug_heap(stderr, HEAP_START);
  _free(data);
  _free(data2);
  print_test_ended();
}

void test_mmap_cannot_extend(){
  print_test_start("malloc if cannot extend");
  void* trash_data = mmap(HEAP_START + REGION_MIN_SIZE, REGION_MIN_SIZE, PROT_NONE, 
                          MAP_PRIVATE | MAP_ANONYMOUS | MAP_FIXED_NOREPLACE, -1, 0);
  assert(trash_data != MAP_FAILED);
  puts("Heap before malloc:");
  debug_heap(stderr, HEAP_START);
  void* data1 = _malloc(REGION_STD_SIZE * 10);
  assert(data1);
  puts("Heap after malloc:");
  debug_heap(stderr, HEAP_START);
  _free(data1);
  print_test_ended();
}


int main(){
  void (*tests[])() = {
    test_simple_allocation,
    test_free_one,
    test_free_all,
    test_mmap_extend,
    test_mmap_cannot_extend
  };
  const size_t test_count = sizeof(tests) / sizeof(tests[0]);

  for(size_t i = 0; i < test_count; i++){
    printf("---------------------------TEST %zu-------------------------------\n", i);
    heap_init(0);
    (tests[i])();
    heap_term();
    printf("---------------------------TEST %zu PASSED------------------------\n", i);
  }
}

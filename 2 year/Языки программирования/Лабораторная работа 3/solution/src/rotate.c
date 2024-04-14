#include "rotate.h"
#include "utilty.h"

#include <stdint.h>
#include <stdlib.h>

size_t static get_array_offset(size_t h, size_t w, size_t width){
  return h * width + w;
}

struct image static rotate90(const struct image src){
  struct image result = create_empty_image(src.height, src.width);
  if(!result.data) return result;

  for(size_t h = 0; h < src.height; h++){
    for(size_t w = 0; w < src.width; w++){
      result.data[get_array_offset(src.width - w - 1, h, src.height)] = src.data[get_array_offset(h, w, src.width)];
    }
  }
  return result;
}

struct image rotate(struct image src, int_fast64_t count){
  for(int_fast64_t i = 0; i < count; i++){
    struct image src_b = rotate90(src);
    free(src.data);
    src = src_b;
    if(!src.data) return src;
  }
  return src;
}



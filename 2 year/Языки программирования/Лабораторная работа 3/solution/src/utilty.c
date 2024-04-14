#include "image.h"

#include <stdint.h>
#include <stdlib.h>

struct image create_empty_image(const uint64_t width, const uint64_t height){
  return (struct image) {
    .height = height,
    .width = width,
    .data = malloc(sizeof(struct pixel) * height * width)
  };
}

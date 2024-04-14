#include <stdint.h>
#pragma once

struct __attribute__((packed)) pixel{
  uint8_t b, g, r;
};


struct image {
  uint64_t width, height;
  struct pixel* data;
};


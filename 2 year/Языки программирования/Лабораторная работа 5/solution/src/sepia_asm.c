#include "../include/image.h"
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>

extern void perform_sepia_pixel(float src[], uint8_t out[]);

struct image perform_sepia_asm(struct image *src) {
  // Fast converting method from llp book
  float byte_to_float[256];
  for (size_t i = 0; i < 256; i++)
    byte_to_float[i] = (float)i;

  struct image out = {
      .height = src->height,
      .width = src->width,
      .data = malloc(sizeof(struct pixel) * src->height * src->width)};
  if (out.data == NULL)
    return out;

  for (size_t i = 0; i < src->width * src->height; i++) {
    float input_array[4];
    uint8_t output_array[4];
    input_array[0] = byte_to_float[src->data[i].r];
    input_array[1] = byte_to_float[src->data[i].g];
    input_array[2] = byte_to_float[src->data[i].b];
    perform_sepia_pixel(input_array, output_array);
    out.data[i].r = output_array[2];
    out.data[i].g = output_array[1];
    out.data[i].b = output_array[0];
  }
  return out;
}

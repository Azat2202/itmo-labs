#include "../include/sepia_c.h"
#include "../include/image.h"
#include <stdint.h>

static uint8_t lowpass(const uint16_t x) { return (x < 256) ? x : 255; }

static const float v[3][3] = {
    // R      G       B
    {.393f, .769f, .189f}, // R
    {.349f, .686f, .168f}, // G
    {.272f, .534f, .131f}  // B
};

static struct pixel perform_sepia_pixel(struct pixel p) {
  float red = p.r;
  float green = p.g;
  float blue = p.b;
  return (struct pixel){
      .r =
          lowpass((uint16_t)(red * v[0][0] + green * v[0][1] + blue * v[0][2])),
      .g =
          lowpass((uint16_t)(red * v[1][0] + green * v[1][1] + blue * v[1][2])),
      .b = lowpass(
          (uint16_t)(red * v[2][0] + green * v[2][1] + blue * v[2][2]))};
}

struct image perform_sepia(struct image *src) {
  struct image out = {
      .width = src->width,
      .height = src->height,
      .data = malloc(sizeof(struct pixel) * src->height * src->width)};
  if (out.data == NULL)
    return out;
  for (size_t i = 0; i < src->width * src->height; i++) {
    out.data[i] = perform_sepia_pixel(src->data[i]);
  }
  return out;
}

#include "bmp.h"
#include <stdbool.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>

#define BMP_BITMAP_ID 0x4D42

uint8_t static get_padding(uint64_t width){
  return (4 - (width * sizeof(struct pixel)) % 4) % 4;
}

enum read_status from_bmp(FILE* in, struct image* img){
  struct bmp_header header = {0};
  if(!fread(&header, sizeof(struct bmp_header), 1, in)) return READ_INVALID_HEADER;
  if(header.bfType != BMP_BITMAP_ID) return READ_INVALID_SIGNATURE;

  img->width = header.biWidth;
  img->height = header.biHeight;
  img->data = malloc(sizeof(struct pixel) * header.biWidth * header.biHeight);
  if(!img->data) return READ_MALLOC_ERROR;

  fseek(in, header.bOffBits, SEEK_SET);

  const uint8_t padding = get_padding(header.biWidth);

  for(uint32_t i = 0; i < header.biHeight; i++){
    if(!fread((img->data) + i * header.biWidth, sizeof(struct pixel), header.biWidth, in)) return READ_INVALID_BITS;
    fseek(in, (long) padding, SEEK_CUR);
  }
  
  return READ_OK;
}


#define BF_TYPE 0x4D42
#define BF_OFF_BITS sizeof(struct bmp_header)
#define BI_SIZE_IMAGE(height, width) (height) * (get_padding((width)) + (width) * sizeof(struct pixel))
#define BF_FILE_SIZE(height, width) (BF_OFF_BITS + BI_SIZE_IMAGE((height), (width))) 
#define BF_RESERVED 0
#define BI_SIZE 40
#define BI_PLANES 1
#define BI_BIT_COUNT 24 
#define BI_COMPRESSION 0
#define BI_X_PELS_PER_METER 2834
#define BI_Y_PELS_PER_METER 2834
#define BI_CLR_USED 0
#define BI_CLR_IMPORTANT 0

#define fill_bmp_header(height, width){\
  header.bfType = BF_TYPE;\
  header.bfileSize = BF_FILE_SIZE(height, width);\
  header.bfReserved = BF_RESERVED;\
  header.bOffBits = BF_OFF_BITS;\
  header.biSize = BI_SIZE;\
  header.biWidth = (width);\
  header.biHeight = (height);\
  header.biPlanes = BI_PLANES;\
  header.biBitCount = BI_BIT_COUNT;\
  header.biCompression = BI_COMPRESSION;\
  header.biSizeImage = BI_SIZE_IMAGE(height, width);\
  header.biXPelsPerMeter = BI_X_PELS_PER_METER;\
  header.biYPelsPerMeter = BI_Y_PELS_PER_METER;\
  header.biClrUsed = BI_CLR_USED;\
  header.biClrImportant = BI_CLR_IMPORTANT;\
}

enum write_status to_bmp(FILE* out, const struct image* img){
  struct bmp_header header = {0};
  fill_bmp_header(img->height, img->width);
  if(!fwrite(&header, sizeof(struct bmp_header), 1, out)) return WRITE_ERROR;

  const uint8_t padding = get_padding(img->width);
  for(size_t i = 0; i < img->height; i++){
    if(fwrite(img->data + img->width * i, sizeof(struct pixel), img->width, out) < img->width) return WRITE_ERROR;
    fseek(out, (long) padding, SEEK_CUR);
  }
  return WRITE_OK;
}


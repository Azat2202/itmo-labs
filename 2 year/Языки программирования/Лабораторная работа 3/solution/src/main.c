#include "bmp.h"
#include "rotate.h"
#include "validador.h"
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>

#define IMAGE_READ_MODE "rb"
#define IMAGE_WRITE_MODE "wb"


int main( int argc, char** argv ) {
  struct parsed_input input = parse_input(argc, argv);
  if(!input.status){
    fprintf(stderr, "Not accessible input\n");
    return 1;
  }
  printf("Input was parsed successfully\n");


  FILE* input_file = fopen(input.source_image, IMAGE_READ_MODE);
  if(!input_file){
    fprintf(stderr, "Input file was not opened\n");
    return 1;
  }
  printf("Input file was openned successfully\n");

  struct image img = {0};
  if(from_bmp(input_file, &img) != READ_OK){
    free(img.data);
    fprintf(stderr, "Cannot read input file\n");
    return 1;
  }
  printf("Input file was read successfully\n");

  if(fclose(input_file) == EOF){
    free(img.data);
    fprintf(stderr, "Input file was not closed!\n");
    return 1;
  }
  printf("Input file was closed successfully\n");


  img = rotate(img, input.count_of_rotations);
  if(!img.data){
    free(img.data);
    fprintf(stderr, "Image was not rotated\n");
    return 1;
  }
  printf("Image was rotated successfully\n");
  

  FILE* output_file = fopen(input.destination_image, IMAGE_WRITE_MODE);
  if(!output_file){
    free(img.data);
    fprintf(stderr, "Output file was not opened successfully\n");
    return 1;
  }
  printf("Output file was opened successfully\n");

  if(to_bmp(output_file, &img) != WRITE_OK){
    fprintf(stderr, "Cannot write to output file\n");
    free(img.data);
    return 1;
  }
  free(img.data);
  printf("Output file was written successfully\n");

  if(fclose(output_file) == EOF){
    fprintf(stderr, "Output file was not closed\n");
    return 1;
  }
  printf("Output file was closed successfully\n");

  return 0;
}

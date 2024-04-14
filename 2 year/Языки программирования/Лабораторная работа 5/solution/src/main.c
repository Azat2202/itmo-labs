#include "../include/bmp.h"
#include "../include/sepia_asm.h"
#include "../include/sepia_c.h"
#include "../include/validador.h"
#include <inttypes.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#define IMAGE_READ_MODE "rb"
#define IMAGE_WRITE_MODE "wb"

int main(int argc, char **argv) {
  struct parsed_input input = parse_input(argc, argv);
  if (!input.status) {
    fprintf(stderr, "Not accessible input\n");
    return 1;
  }
  if(input.count_of_tests <= 0){
    fprintf(stderr, "Count of tests must be more than 0\n");
    return 1;
  }

  FILE *input_file = fopen(input.source_image, IMAGE_READ_MODE);
  if (!input_file) {
    fprintf(stderr, "Input file was not opened\n");
    return 1;
  }

  struct image img = {0};
  if (from_bmp(input_file, &img) != READ_OK) {
    free(img.data);
    fprintf(stderr, "Cannot read input file\n");
    return 1;
  }

  if (fclose(input_file) == EOF) {
    free(img.data);
    fprintf(stderr, "Input file was not closed!\n");
    return 1;
  }

  printf("Perfoming tests %" PRId64 " times\n", input.count_of_tests);
  double c_time = 0;
  double asm_time = 0;
  // Testing c
  for (size_t i = 0; i < input.count_of_tests; i++) {
    clock_t test_start, test_end;
    test_start = clock();
    struct image out = perform_sepia(&img);
    test_end = clock();
    c_time += (double)(test_end - test_start) / CLOCKS_PER_SEC;

    free(out.data);
  }
  for (size_t i = 0; i < input.count_of_tests; i++) {
    clock_t test_start, test_end;
    test_start = clock();
    struct image out = perform_sepia_asm(&img);
    test_end = clock();
    asm_time += (double)(test_end - test_start) / CLOCKS_PER_SEC;

    free(out.data);
  }

  puts("Average timing per test:");
  printf("C --------- %lf seconds\n", c_time / (double)(input.count_of_tests));
  printf("Asm ------- %lf seconds\n",
         asm_time / (double)(input.count_of_tests));

  struct image out = perform_sepia(&img);

  free(img.data);
  img.data = out.data;

  FILE *output_file = fopen(input.destination_image, IMAGE_WRITE_MODE);
  if (!output_file) {
    free(img.data);
    fprintf(stderr, "Output file was not opened successfully\n");
    return 1;
  }

  if (to_bmp(output_file, &img) != WRITE_OK) {
    fprintf(stderr, "Cannot write to output file\n");
    free(img.data);
    return 1;
  }
  free(img.data);

  if (fclose(output_file) == EOF) {
    fprintf(stderr, "Output file was not closed\n");
    return 1;
  }

  return 0;
}

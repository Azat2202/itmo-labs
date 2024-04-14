#include <stdint.h>
#pragma once

struct parsed_input;
struct parsed_input parse_input(int args, char** argv);

enum parsed_input_status{
  WRONG_AMOUNT_OF_CHARS = 0,
  PARSED_SUCCESSFULLY = 1
};

struct parsed_input{
  enum parsed_input_status status;
  char* source_image;
  char* destination_image;
  int_fast64_t count_of_rotations;
};


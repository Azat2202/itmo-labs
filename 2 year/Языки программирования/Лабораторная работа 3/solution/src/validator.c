#include "validador.h"

#include <stdint.h>
#include <stdlib.h>

const struct parsed_input wrong_amount_of_chars = {0};

int_fast64_t static parse_angle(char* str){
  int_fast64_t x = strtol(str, NULL, 10);
  if(x < 0) x = 360 + x;
  if(x > 360) return 0;
  return x / 90;
}

struct parsed_input parse_input(int args, char** argv){
  if(args != 4){
    return wrong_amount_of_chars;
  }

  return (struct parsed_input){
    PARSED_SUCCESSFULLY,
    argv[1],
    argv[2],
    parse_angle(argv[3])
  };
}


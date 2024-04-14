#include "../include/validador.h"

#include <stdint.h>
#include <stdlib.h>

const struct parsed_input wrong_amount_of_chars = {0};

struct parsed_input parse_input(int args, char **argv) {
  if (args != 4) {
    return wrong_amount_of_chars;
  }

  return (struct parsed_input){PARSED_SUCCESSFULLY, argv[1], argv[2],
                               strtol(argv[3], NULL, 10)};
}

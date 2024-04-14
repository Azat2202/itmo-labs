; vim: ft=nasm:
%include "colon.inc"

section .rodata

colon "last word", last_word
db "last word explanation", 0

colon "LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL", fourth_word
db "L word explanation", 0

colon "third word", third_word
db "third word explanation", 0

colon "second word", second_word
db "second word explanation", 0 

colon "first word", first_word
db "first word explanation", 0 


; vim: ft=nasm:
%include "words.inc"
%include "lib.inc"
%include "dict.inc"

%define BUFFER_LENGTH 256

section .rodata
    cannot_find:   db "That label doesn`t exists", 0
    cannot_read:    db "The buffer is overflowed. Max - 255 symbols", 0

section .bss
    buffer:     resb BUFFER_LENGTH

section .text
global _start

_start:
    mov     rdi, buffer
    mov     rsi, BUFFER_LENGTH
    call    read_line
    mov     r12, rdx
    
    test    rax, rax
    jz      .exit_cannot_read
    
    mov     rdi, rax
    mov     rsi, first_word

    call    find_word
    test    rax, rax
    jz      .exit_cannot_found
    mov     rdi, rax
    add     rdi, r12
    inc     rdi
    call    print_string
    call    print_newline
    xor     rdi, rdi            ; normal exit code
    jmp     exit

    .exit_cannot_found:
        mov     rdi, cannot_find
        jmp     .exit_fail

    .exit_cannot_read:
        mov     rdi, cannot_read
    .exit_fail:
        call    print_error
        call    print_newline
        mov     rdi, 1          ; error exit code
        jmp     exit


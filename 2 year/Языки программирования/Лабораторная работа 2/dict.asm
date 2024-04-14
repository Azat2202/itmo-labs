; vim:ft=nasm:

%include "lib.inc"

section .text

global find_word


; input:
; rdi - null-terminated stirng ptr
; rsi - first element of list ptr

; output:
; rax - adress of element if found, else 0
find_word:
    xor     rax, rax    ;If first element is zero pointer we must return 0
    .loop:
        push    rsi
        push    rdi
        lea     rsi, [rsi + 8]      ; actually, similar to add rsi, 8
        
        call    string_equals

        pop     rdi
        pop     rsi
        test    rax, rax
        jnz     .exit_found
        cmp     qword[rsi], 0      ; w\ test because `test [rsi], [rsi]` is restricted
        je      .exit_not_found
       
        mov     rsi, [rsi]          ; go to next element of array
        jmp     .loop

    .exit_found:
        mov     rax, rsi
        add     rax, 8
    .exit_not_found:     ;During runtime while we havent found element we only have zero
        ret

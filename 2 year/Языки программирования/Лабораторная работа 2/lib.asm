; vim:ft=nasm:

%define READ_SYSCALL 0
%define PRINT_SYSCALL 1
%define STDIN 0
%define STDOUT 1
%define STDERR 2

section .text

global exit
global string_length
global print_string
global print_error
global print_char
global print_newline
global print_uint
global print_int
global string_equals
global read_char
global read_word
global read_line
global parse_uint
global parse_int
global string_copy

; Принимает код возврата и завершает текущий процесс
exit: 
	mov rax, 60
	syscall

; Принимает указатель на нуль-терминированную строку, возвращает её длину
string_length:  
	xor	rax, rax
	.loop:
		cmp	byte [rdi + rax], 0
		jz	.break
		inc 	rax
		jmp	.loop
	.break:	
		ret

; Принимает указатель на нуль-терминированную строку, выводит её в stdout
print_string:
	push	rdi
	call	string_length
	pop	rsi
	mov	rdx, rax
	mov	rax, PRINT_SYSCALL
	mov	rdi, STDOUT
	syscall
    	ret

;--------------
;----CUSTOM----
;--------------
; Принимает указатель на нуль-терминированную строку, выводит её в stderr
print_error:
	push	rdi
	call	string_length
	pop	rsi
	mov	rdx, rax
	mov	rax, PRINT_SYSCALL
	mov	rdi, STDERR
	syscall
    	ret

; Переводит строку (выводит символ с кодом 0xA)
print_newline:
	mov	rdi, 0xA


; Принимает код символа и выводит его в stdout
print_char:
	push	rdi
	mov	rsi, rsp
	mov	rdx, 1
	mov	rax, PRINT_SYSCALL
	mov	rdi, STDOUT
	syscall
	pop	rdi
	ret


; Выводит беззнаковое 8-байтовое число в десятичном формате 
; Совет: выделите место в стеке и храните там результаты деления
; Не забудьте перевести цифры в их ASCII коды.
print_uint:
	mov	rax, rdi
	mov	r8, 10
	mov	rcx, rsp
	sub	rsp, 24
	dec	rcx
	mov	byte [rcx], 0	;null-terminator
	.loop:
		xor	rdx, rdx
		div	r8
		add	dl, '0'
		dec	rcx
		mov	byte [rcx], dl
		test	rax, rax
		jnz	.loop
	mov	rdi, rcx
	call	print_string
	add	rsp, 24
	ret



; Выводит знаковое 8-байтовое число в десятичном формате 
print_int:
	test	rdi, rdi
	jns	print_uint
	.neg:
		push	rdi
		mov	rdi, '-'
		call	print_char
		pop	rdi
		neg	rdi
	.print:
		jmp	print_uint



; Принимает два указателя на нуль-терминированные строки, возвращает 1 если они равны, 0 иначе
string_equals:
	xor 	rcx,	rcx
	.loop:
		lodsb
		cmp	[rdi + rcx],  al
		jne	.bad
		inc	rcx
		test	al, al
		jnz	.loop
	.good:
		mov	rax, 1
		ret
	.bad:	
		xor	rax, rax
		ret


; Читает один символ из stdin и возвращает его. Возвращает 0 если достигнут конец потока
read_char:
	sub	rsp, 8
	mov	rax, READ_SYSCALL
	mov	rdi, STDIN
	mov	rsi, rsp
	mov	rdx, 1
	syscall
	cmp	rax, 0
	jle	.return
	mov	rax, [rsp]
	.return:
		add	rsp, 8
		ret

;----------------
;-----CUSTOM-----
;----------------


; Принимает: адрес начала буфера, размер буфера
; Читает в буфер строку из stdin, пропуская пробельные символы в начале, .
; Пробельные символы это пробел 0x20, табуляция 0x9 и перевод строки 0xA.
; Останавливается и возвращает 0 если слово слишком большое для буфера
; При успехе возвращает адрес буфера в rax, длину слова в rdx.
; При неудаче возвращает 0 в rax
; Эта функция должна дописывать к слову нуль-терминатор

read_line:
	push	rbx
	push	r12
	push	r13
	mov	rbx, -1		;Save space for null-terminator
	mov	r12, rsi
	mov	r13, rdi
	.read_spaces:
		call	read_char
		test	al, al
		jz	.end_bad
		cmp 	al, ` `
		je	.read_spaces
		cmp	al, `\n`
		je	.read_spaces
		cmp 	al, `\t`
		je	.read_spaces
		jmp	.skip_test
	.read_word:
		cmp	al, `\n`
		je	.end_good
		cmp	al, `\t`
		je	.end_good
		test	al, al
		je	.end_good
	.skip_test:			;Not to check letter twice
		inc 	rbx
		cmp	rbx, r12
		je	.end_bad
		mov	byte [r13 + rbx], al
		call	read_char
		jmp	.read_word
	.end_good:
		inc	rbx
		mov	byte [r13 + rbx], 0
		mov	rax, r13
		mov	rdx, rbx
		pop	r13
		pop	r12
		pop	rbx
		ret
	.end_bad:
		xor 	rax, rax
		xor	rdx, rdx
		pop	r13
		pop 	r12
		pop	rbx
		ret



; Принимает: адрес начала буфера, размер буфера
; Читает в буфер слово из stdin, пропуская пробельные символы в начале, .
; Пробельные символы это пробел 0x20, табуляция 0x9 и перевод строки 0xA.
; Останавливается и возвращает 0 если слово слишком большое для буфера
; При успехе возвращает адрес буфера в rax, длину слова в rdx.
; При неудаче возвращает 0 в rax
; Эта функция должна дописывать к слову нуль-терминатор

read_word:
	push	rbx
	push	r12
	push	r13
	mov	rbx, -1		;Save space for null-terminator
	mov	r12, rsi
	mov	r13, rdi
	.read_spaces:
		call	read_char
		test	al, al
		jz	.end_bad
		cmp 	al, ` `
		je	.read_spaces
		cmp	al, `\n`
		je	.read_spaces
		cmp 	al, `\t`
		je	.read_spaces
		jmp	.skip_test
	.read_word:
		cmp	al, ` `
		je	.end_good
		cmp	al, `\n`
		je	.end_good
		cmp	al, `\t`
		je	.end_good
		test	al, al
		je	.end_good
	.skip_test:			;Not to check letter twice
		inc 	rbx
		cmp	rbx, r12
		je	.end_bad
		mov	byte [r13 + rbx], al
		call	read_char
		jmp	.read_word
	.end_good:
		inc	rbx
		mov	byte [r13 + rbx], 0
		mov	rax, r13
		mov	rdx, rbx
		pop	r13
		pop	r12
		pop	rbx
		ret
	.end_bad:
		xor 	rax, rax
		xor	rdx, rdx
		pop	r13
		pop 	r12
		pop	rbx
		ret

; Принимает указатель на строку, пытается
; прочитать из её начала беззнаковое число.
; Возвращает в rax: число, rdx : его длину в символах
; rdx = 0 если число прочитать не удалось
parse_uint:
	xor	rdx, rdx
	xor	rax, rax
	xor	rcx, rcx
	.read_next:
		mov	al, [rdi + rcx]
		test	al, al
		jz	.good_exit
		sub	al, '0'		;Trick that takes one command less
		js	.bad_exit
		cmp	al, 0x9
		ja	.bad_exit
		inc	rcx
		imul	rdx, 10
		add	dl, al
		jmp	.read_next
	.good_exit:
		mov	rax, rdx
		mov	rdx, rcx
		ret
	.bad_exit:
		mov	rax, rdx
		mov	rdx, rcx
		ret



; Принимает указатель на строку, пытается
; прочитать из её начала знаковое число.
; Если есть знак, пробелы между ним и числом не разрешены.
; Возвращает в rax: число, rdx : его длину в символах (включая знак, если он был) 
; rdx = 0 если число прочитать не удалось
parse_int:
	mov	al, [rdi]
	cmp	al, '-'
	je 	.neg
	cmp	al, '+'
	jne	.pos
	inc	rdi
	.pos:
		jmp	parse_uint
	.neg:
		inc	rdi
		call	parse_uint
		neg	rax
		inc	rdx
		ret



; Принимает указатель на строку (rdi), указатель на буфер (rsi) и длину буфера (rdx)
; Копирует строку в буфер
; Возвращает длину строки если она умещается в буфер, иначе 0
string_copy:
	push	rdi
	push	rsi
	push	rdx
	call	string_length
	pop	rcx		;rdx -> rcx
	pop	rdi		;rsi -> rdi
	pop	rsi		;rdi -> rsi
	
	cmp	rdx, rax
	jl	.bad
	.good:	
		rep movsb
		ret
	.bad:
		xor	rax, rax
		ret




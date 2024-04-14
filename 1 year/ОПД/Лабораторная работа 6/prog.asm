	ORG	0x0
v0:	WORD	$default, 	0x180
v1:	WORD	$default, 	0x180
v2:	WORD	$int2, 	0x180
v3:	WORD	$int3, 	0x180
v4:	WORD	$default, 	0x180
v5:	WORD	$default, 	0x180
v6:	WORD	$default, 	0x180
v7:	WORD	$default, 	0x180

default:	IRET

int2:	NOP
	PUSH		;Сохранение аккумулятора
	IN	0x4
	NEG
	CALL	CHECK_ODZ
	ST	$X
	NOP
	POP
	NOP		;Проверка значения аккумулятора
	IRET

int3:	NOP
	PUSH		;Сохранение аккумулятора
	LD	$X
	NOP		;Отладочная метка
	ASL
	ADD	$X	;Вычисление f(x) = 3x+6
	ADD	#0x6
	OUT	0x6
	POP
	NOP		;Проверка значения аккумулятора
	IRET

	ORG	0x039
X:	WORD	0x0
X_MAX:	WORD	0x0028	;Верхняя граница ОДЗ (включительно)
X_MIN:	WORD	0xFFD4	;Нижняя граница ОДЗ (включительно)

	ORG	0x50
START:	DI
	CLA
	LD	#0xA	;Загрузка в AC MR(1000|0010 = 1010)
	OUT	0x5	;Разрешение прерываний для ВУ-2
	LD	#0xB	;Загрузка в AC MR(1000|0011 = 1011)
	OUT	0x7	;Разрешение прерываний для ВУ-3
	EI

MAIN:	DI		;Запрет прерываний для атомарности операции
	LD	$X	;Загружаем Х
	SUB	#0x3	;Вычитаем 3
	CALL	CHECK_ODZ
	ST	$X	
	EI
	BR	MAIN


;Подпрограмма проверки ОДЗ, принимает и возвращает число в аккумуляторе
CHECK_ODZ:	CMP	X_MIN
	BLT	CHECK_BAD	;Если строго меньше минимума то вне ОДЗ
	CMP	X_MAX
	BEQ	CHECK_OK	;Если строго больше максимума то вне ОДЗ
	BGE	CHECK_BAD
CHECK_OK:	RET
CHECK_BAD:	LD	X_MAX
	RET

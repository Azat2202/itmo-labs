		ORG		0x3F1
START:	CLA 
	LD RES_ADDR
	ST RES_ADDR_BUF

;Read count of letters

S1:	IN 5
	AND #0X40
	BEQ S1 ;spin-loop
	IN 4
	ST (RES_ADDR_BUF)+
	ST LOOP_CNT
	
;Read highest byte
S2:	IN	5
	AND	#0x40
	BEQ S2 ;spin-loop
	
	IN	4
	SWAB
	ST 	BUF

;Read lowest byte
S3:	IN 	5
	AND #0x40
	BEQ S3 ;spin-loop
	
	LD 	BUF
	IN 	4
	ST 	(RES_ADDR_BUF)+
	LOOP LOOP_CNT
	JUMP S2
	HLT
RES_ADDR:	WORD 0X63B ;ссылка на адрес первого элемента
RES_ADDR_BUF:	WORD	?
LOOP_CNT:	WORD	?
BUF:	WORD	?

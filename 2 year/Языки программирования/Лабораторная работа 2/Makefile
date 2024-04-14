ASM=nasm
ASMFLAGS=-felf64

LD=ld
LDFLAGS= 

RM=rm
RMFLAGS=

PYTHON = python3
PYTHON_FLAGS = 

TEST_STDIN = ("first word", "last word")
TEST_STDOUT = ('first word explanation', 'second word explanation')

.PHONY: clean main test

all:	main


main.o: main.asm lib.inc words.inc dict.inc
dict.o: dict.asm lib.inc
words.inc: colon.inc

%.o: %.asm
	$(ASM) -g $< $(ASMFLAGS) -o $@  

main:	lib.o dict.o main.o
	$(LD) $(LDFLAGS) -o $@ $^

test: main
	$(PYTHON) $(PYTHON_FLAGS) test.py

clean:	
	$(RM) $(RMFLAGS) main.o lib.o dict.o

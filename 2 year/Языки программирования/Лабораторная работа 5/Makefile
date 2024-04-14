.PHONY: build clean

CC=gcc
C_FLAGS=-I $(INCLUDE_DIR) -Wall -Werror -g

ASM=nasm
ASM_FLAGS=-felf64 

SRC_DIR=solution/src
INCLUDE_DIR=solution/include
H_FILES=$(wildcard $(INCLUDE_DIR)/*.h)

OBJ_DIR=obj
OUT_FILE=$(OBJ_DIR)/main
OBJ_FILES = $(patsubst $(SRC_DIR)/%.c, $(OBJ_DIR)/%.o, $(wildcard $(SRC_DIR)/*.c))
OBJ_FILES+= $(patsubst $(SRC_DIR)/%.asm, $(OBJ_DIR)/%.asm.o, $(wildcard $(SRC_DIR)/*.asm))

MKDIR=mkdir
MKDIR_FLAGS=-p

FORMAT=clang-format
FORMAT_STYLE=LLVM
FORMAT_FLAGS=-style=$(FORMAT_STYLE) -i
TIDY=clang-tidy

PYTHON=python3
TEST_FILE=tester/test.py

RM=rm -rf

.PHONY: build clean test format tidy

build: $(OUT_FILE);

$(OUT_FILE): $(OBJ_FILES)
	$(CC) -o $@ $(OBJ_FILES) -no-pie

$(OBJ_DIR)/%.asm.o: $(SRC_DIR)/%.asm
	$(MKDIR) $(MKDIR_FLAGS) $(OBJ_DIR)
	$(ASM) $(ASM_FLAGS) -o $@ $<

$(OBJ_DIR)/%.o: $(SRC_DIR)/%.c $(H_FILES)
	$(MKDIR) $(MKDIR_FLAGS) $(OBJ_DIR)
	$(CC) $(C_FLAGS) -c -o $@ $<

clean:
	$(RM) $(OBJ_DIR)

test: build
	$(PYTHON) $(TEST_FILE)

format:
	$(FORMAT) $(FORMAT_FLAGS) $(SRC_DIR)/*.c $(INCLUDE_DIR)/*.h

tidy:
	$(TIDY) $(SRC_DIR)/*.c $(INCLUDE_DIR)/*.h

import sys
from typing import AnyStr

from ak_lab3.isa import Instruction, Opcode, write_code


def remove_comment(line: str) -> str:
    return line.split(";", 1)[0].strip()


def parse_opcode(code: str) -> Opcode:
    return Opcode[code]


def parse_word_data(data: str) -> list[Instruction]:
    if data.startswith("0x"):
        return [Instruction(Opcode.WORD, int(data[2:], 16))]
    return [Instruction(Opcode.WORD, ord(ch)) for ch in data.strip('"') + "\0"]


def preprocess(input_data: AnyStr):
    """Remove comments, replace tabs with spaces, only one space and empty lines"""
    data = [remove_comment(str(line_raw)) for line_raw in input_data.splitlines()]
    data = [line.replace("\t", " ") for line in data]
    data = [" ".join(line.split()) for line in data]
    data = [line for line in data if line != ""]
    return "\n".join(data)


def translate_stage_1(input_data: str) -> tuple[list[Instruction], dict[str, int]]:
    """Remove all commits and split into terms, without links"""
    code: list[Instruction] = []
    labels: dict[str, int] = {}
    for line_raw in input_data.splitlines():
        line = remove_comment(str(line_raw))
        if line[-1] == ":":
            assert line.count(" ") == 0, "Label and text must be on different lines"
            labels.update({line[:-1]: len(code)})
        elif line.count(" ") == 0:
            code.append(Instruction(parse_opcode(line)))
        elif line.startswith("PUSH"):
            op, arg = line.split(" ", 2)
            code.append(Instruction(parse_opcode(op), arg))
        elif line.startswith("WORD"):
            _, data = line.split(" ", 1)
            code += parse_word_data(data)
    return code, labels


def translate_stage_2(
    code: list[Instruction], labels: dict[str, int]
) -> tuple[list[Instruction], int]:
    for i, instr in enumerate(code):
        if instr.opcode != Opcode.PUSH:
            continue
        if isinstance(instr.arg, str) and instr.arg.startswith("0x"):
            instr.arg = int(instr.arg, 16)
            continue
        assert code[i].arg in labels, f"None such label: {code[i].arg}"
        code[i].arg = labels[str(code[i].arg)]
    assert "_start" in labels, "No label start in code!"
    return code, labels["_start"]


def translate(input_data: AnyStr) -> dict:
    input_str = preprocess(input_data)
    code, labels = translate_stage_1(input_str)
    code, start = translate_stage_2(code, labels)
    return {
        "start": start,
        "code": code,
    }


def main(source_file: str, target_file: str):
    with open(source_file, "r", encoding="utf-8") as file:
        input_data = file.read()

    code = translate(input_data)
    write_code(target_file, code)


if __name__ == "__main__":
    assert (
            len(sys.argv) == 3
    ), "Wrong arguments: translator.py <input_file> <output_file>"
    _, source, target = sys.argv
    main(source, target)

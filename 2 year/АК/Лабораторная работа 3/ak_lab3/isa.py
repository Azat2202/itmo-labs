import json
from dataclasses import dataclass, asdict
from enum import Enum, StrEnum, auto
from typing import Optional


class Opcode(StrEnum):
    DUP = auto()
    OVER = auto()
    POP = auto()
    SWAP = auto()
    LOAD = auto()
    SAVE = auto()
    INC = auto()
    DEC = auto()
    ADD = auto()
    SUB = auto()
    DIV = auto()
    XOR = auto()
    MUL = auto()
    JUMP = auto()
    JZ = auto()
    PUSH = auto()
    WORD = auto()
    HALT = auto()
    TEST = auto()


@dataclass
class Instruction:
    opcode: Opcode
    arg: Optional[int | str] = None

    def to_dict(self):
        data = asdict(self)
        return {key: value for key, value in data.items() if value is not None}

    @staticmethod
    def from_dict(data: dict):
        assert "opcode" in data, "Parsing Instruction failed! Check translator"
        return Instruction(data["opcode"], data["arg"] if "arg" in data else None)

    def __str__(self) -> str:
        return str(self.to_dict())


class CustomEncoder(json.JSONEncoder):
    def default(self, o):
        if isinstance(o, Instruction):
            return o.to_dict()
        if isinstance(o, Enum):
            return o.value
        return json.JSONEncoder.default(self, o)


def write_code(filename: str, code: dict) -> None:
    with open(filename, "w", encoding="utf-8") as file:
        file.write(json.dumps(code, cls=CustomEncoder, indent=2))


def read_code(filename: str) -> dict[str, str]:
    return json.load(open(filename, encoding="utf-8"))

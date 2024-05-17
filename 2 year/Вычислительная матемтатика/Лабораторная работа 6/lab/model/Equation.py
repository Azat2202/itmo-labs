from dataclasses import dataclass
from typing import Callable


@dataclass
class Equation:
    equation: Callable[[float, float], float]
    solved: Callable[[float], float]
    equation_str: str

    def __str__(self) -> str:
        return self.equation_str

    def __repr__(self) -> str:
        return self.equation_str

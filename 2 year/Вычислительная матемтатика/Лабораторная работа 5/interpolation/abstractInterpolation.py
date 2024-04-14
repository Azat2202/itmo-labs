from typing import Callable, List

from data import Point


class AbstractInterpolation:
    name = ""
    f: Callable[[float], float]
    n: int

    def __init__(self, table: List[Point]):
        self.table = table
        self.n = len(table)
        self.f = self.create_function()

    def at(self, x: float):
        return self.f(x)

    def create_function(self) -> Callable[[float], float]:
        raise NotImplementedError

    @staticmethod
    def check(table: List[Point]) -> bool:
        return True

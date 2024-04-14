from typing import List

from approximations import Point
from approximations.AnyFunction import AnyFunction
from approximations.Approximation import Approximation


class Third(AnyFunction):
    name = "Аппроксимация степени 3"
    def __init__(self, table: List[Point]) -> None:
        super().__init__(table, 3)


class Fourth(AnyFunction):
    name = "Аппроксимация степени 4"
    def __init__(self, table: List[Point]) -> None:
        super().__init__(table, 4)


class Fifth(AnyFunction):
    name = "Аппроксимация степени 5"
    def __init__(self, table: List[Point]) -> None:
        super().__init__(table, 5)


class Sixth(AnyFunction):
    name = "Аппроксимация степени 6"
    def __init__(self, table: List[Point]) -> None:
        super().__init__(table, 6)

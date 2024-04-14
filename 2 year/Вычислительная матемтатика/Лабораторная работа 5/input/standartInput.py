from typing import List

from data import Point
from input import AbstractInput


class StandartInput(AbstractInput):
    name = "набор данных"

    def read(self) -> list[Point]:
        line = input("Вводите точки в формате 'x y'\n")
        out: List[Point] = list()
        while line.strip():
            try:
                out.append(Point(*map(float, line.strip().split())))
                line = input()
            except ValueError as _:
                print("Невалидные координаты!")
        return out

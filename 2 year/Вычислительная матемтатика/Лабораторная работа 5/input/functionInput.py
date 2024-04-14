import math
from dataclasses import dataclass
from typing import Callable, Iterator, List

import numpy as np

from data import Point
from input import AbstractInput


@dataclass
class Function:
    f: Callable[[float], float]
    name: str


def sign(x: float):
    if x > 0:
        return 1
    if x < 0:
        return -1
    return 0


class FunctionInput(AbstractInput):
    name = "выбранная функция"
    function_list: List[Function] = [
        Function(sign, "sign(x)"),
        Function(math.sin, "sin(x)")
    ]

    def read(self) -> list[Point]:
        while True:
            try:
                print("Выберите функцию:")
                print("".join(f"{i}: {func.name}\n" for i, func in enumerate(self.function_list)), end="")
                i = int(input())
                f = self.function_list[i].f
                a = float(input("Введите левую границу интервала: "))
                b = float(input("Введите правую границу интервала: "))
                h = float(input("Введите шаг интерполирования: "))
                break
            except ValueError:
                print("Ввод не распознан")
            except IndexError:
                print("Такой функции нет в списке")
        return [
            Point(x=x, y=f(x)) for x in np.linspace(a, b, num=math.floor((b - a) / h) + 1)
        ]

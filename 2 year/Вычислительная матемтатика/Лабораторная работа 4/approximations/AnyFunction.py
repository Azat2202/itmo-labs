import math
from typing import Callable, List

import numpy as np
from prettytable import PrettyTable

from approximations import Point
from approximations.Approximation import Approximation
from matrixsolver.IterationMethod import IterationMethod
from matrixsolver.gauss import Gauss


# доп: аппроксимация многочленом любой степени
class AnyFunction(Approximation):
    name = "Аппроксимация любым многочленом"
    view = ""

    def __init__(self, table: List[Point], m) -> None:
        super().__init__(table)
        self.m = m
        letters = "abcdefghijklmnopqrstuvwxyz"
        self.view = " +".join([f"{letters[m - i]}x^{i}" for i in range(m, -1, -1)])
        self.name = f"Аппроксимация степени {m}"

    def solve(self, file) -> Callable[[float], float]:
        SX = [self.n] + [sum([p.x ** i for p in self.table])
                         for i in range(1, self.m * 2 + 1)
         ]
        SY = [
            sum([p.y * p.x ** i for p in self.table])
            for i in range(self.m + 1)
        ]
        # print("any", self.m)
        # print(SX, sep="\n")
        x = [
            SX[i:i + self.m + 1]
            for i in range(self.m + 1)
        ]

        y = SY
        # print(*x, sep="\n")
        # print(y)
        # a = np.linalg.solve(x, y)
        # print(*x, sep="\n")
        # print(y)
        a = Gauss(x, y).solve()

        # self.func = lambda x: a[0] + a[1] * x + a[2] * x ** 2 + a[3] * x ** 3
        print(f"Коэффициенты для степени {self.m}: ", *a[::-1])
        self.func = lambda x: sum([
            a[i] * x ** i
            for i in range(self.m + 1)
        ])
        # self.d, self.c, self.b, self.a = a
        self.report(file=file)
        return self.func

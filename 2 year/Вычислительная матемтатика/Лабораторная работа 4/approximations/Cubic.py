import math
from typing import Callable

import numpy as np
from prettytable import PrettyTable

from approximations.Approximation import Approximation
from matrixsolver.IterationMethod import IterationMethod
from matrixsolver.gauss import Gauss

class Cubic(Approximation):
    name = "Кубическая  аппроксимация"
    view = "ax^3 + bx^2 + cx + d"

    def solve(self, file) -> Callable[[float], float]:
        SX = sum([p.x for p in self.table])
        SX2 = sum([p.x**2 for p in self.table])
        SX3 = sum([p.x**3 for p in self.table])
        SX4 = sum([p.x**4 for p in self.table])
        SX5 = sum([p.x**5 for p in self.table])
        SX6 = sum([p.x**6 for p in self.table])
        SY = sum([p.y for p in self.table])
        SXY = sum([p.x * p.y for p in self.table])
        SX2Y = sum([p.x**2 * p.y for p in self.table])
        SX3Y = sum([p.x**3 * p.y for p in self.table])

        x = [
            [self.n, SX, SX2, SX3],
            [SX, SX2, SX3, SX4],
            [SX2, SX3, SX4, SX5],
            [SX3, SX4, SX5, SX6]
        ]
        y = [
            SY,
            SXY,
            SX2Y,
            SX3Y
        ]
        print("3")
        print(x, sep="\n")
        print(y)

        # a = np.linalg.solve(x, y)
        # print(*x, sep="\n")
        # print(y)
        # доп: решать систему методом гаусса
        a = Gauss(x, y).solve()

        self.func = lambda x: a[0] + a[1] * x + a[2] * x**2 + a[3] * x**3
        self.d, self.c, self.b, self.a = a
        self.report(file=file)
        return self.func

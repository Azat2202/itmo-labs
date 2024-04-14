import math
from typing import Callable

import numpy as np
from prettytable import PrettyTable

from approximations.Approximation import Approximation


class Quadratic(Approximation):
    name = "Квадратичная аппроксимация"
    view = "ax^2 + bx + c"

    def solve(self, file) -> Callable[[float], float]:
        SX = sum([p.x for p in self.table])
        SX2 = sum([p.x**2 for p in self.table])
        SX3 = sum([p.x**3 for p in self.table])
        SX4 = sum([p.x**4 for p in self.table])
        SY = sum([p.y for p in self.table])
        SXY = sum([p.x * p.y for p in self.table])
        SX2Y = sum([p.x**2 * p.y for p in self.table])

        x = np.array([
            [self.n, SX, SX2],
            [SX, SX2, SX3],
            [SX2, SX3, SX4]
        ])
        y = np.array([
            SY,
            SXY,
            SX2Y
        ])
        a = np.linalg.solve(x, y)

        self.func = lambda x: a[0] + a[1] * x + a[2] * x**2
        self.c, self.b, self.a = a
        self.report(file=file)
        return self.func

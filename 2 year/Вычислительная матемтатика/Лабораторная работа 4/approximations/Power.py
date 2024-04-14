import math
from typing import Callable

import numpy as np
from prettytable import PrettyTable

from approximations.Approximation import Approximation
from approximations.Linear import Linear
from approximations.Point import Point


class Power(Approximation):
    name = "Аппроксимация степенной функцией"
    view = "ax^b"

    def check(self) -> bool:
        if not super().check():
            return False
        return all([p.x > 0 for p in self.table]) and all([p.y > 0 for p in self.table])

    def solve(self, file) -> Callable[[float], float]:
        table_transited = [
            Point(
                x=np.log(p.x),
                y=np.log(p.y)
            )
            for p in self.table
        ]
        b_, a_, = Linear(table_transited).get_linear_ratio()
        a = np.exp(a_)
        b = b_
        self.func = lambda x: a * x ** b
        self.a, self.b = a, b
        self.report(file=file)
        return self.func

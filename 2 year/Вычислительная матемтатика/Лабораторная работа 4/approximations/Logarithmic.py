import math
from typing import Callable

import numpy as np
from prettytable import PrettyTable

from approximations.Approximation import Approximation
from approximations.Linear import Linear
from approximations.Point import Point


class Logarithmic(Approximation):
    name = "Аппроксимация логарифмической функцией"
    view = "alnx + b"

    def check(self) -> bool:
        if not super().check():
            return False
        return all([p.x > 0 for p in self.table])

    def solve(self, file) -> Callable[[float], float]:
        table_transited = [
            Point(
                x=np.log(p.x),
                y=p.y
            )
            for p in self.table
        ]
        a_, b_, = Linear(table_transited).get_linear_ratio()
        self.func = lambda x: a_ * np.log(x) + b_
        self.a, self.b = a_, b_
        self.report(file=file)
        return self.func

import math
from typing import Callable

from interpolation import AbstractInterpolation


class Lagrange(AbstractInterpolation):
    name = "интерполяция Лагранжа"

    def create_function(self) -> Callable[[float], float]:
        return lambda x: sum(
            yi * math.prod(
                (x - xj) / (xi - xj)
                for xj, yj in self.table
                if xj != xi                 # assuming x are unique this is eqv of i != j
            )
            for xi, yi in self.table
        )

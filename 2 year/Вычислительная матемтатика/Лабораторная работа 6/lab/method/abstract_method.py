from typing import Callable


class AbstractMethod:
    name = ""
    def __init__(self,
                 x0: float,
                 y0: float,
                 xn: float,
                 h: float,
                 e: float,
                 f: Callable[[float, float], float],
                 f_ac: Callable[[float], float]):
        self.x0 = x0
        self.y0 = y0
        self.xn = xn
        self.h = h
        self.e = e
        self.f = f
        self.f_ac = f_ac

    def solve(self) -> list[float]:
        raise NotImplementedError()

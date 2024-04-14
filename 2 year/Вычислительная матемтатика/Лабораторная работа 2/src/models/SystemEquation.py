from typing import Callable
import matplotlib.pyplot as plt
import numpy as np
from scipy.misc import derivative
from sympy import symbols, plot_implicit, Eq
from sympy.utilities.lambdify import implemented_function

dx = 0.00001


class SystemEquation:
    def __init__(self, f1: Callable[[float, float], float], f2: Callable[[float, float], float], text1: str,
                 text2: str):
        self.text1 = text1
        self.text2 = text2
        self.f1 = f1
        self.f2 = f2

    def draw(self, x_start: float, x_end: float, y_start: float, y_end: float) -> None:
        x, y = symbols("x y")
        p1 = plot_implicit(Eq(self.f1(x, y), 0), (x, x_start, x_end), (y, y_start, y_end), line_color='blue',
                           adaptive=False, show=False)
        p2 = plot_implicit(Eq(self.f2(x, y), 0), (x, x_start, x_end), (y, y_start, y_end), line_color='red',
                           adaptive=False, show=False)
        p1.append(p2[0])
        p1.show()

import math
import sys

import numpy as np

from lab.console_worker import ask_for_equation, ask_float
from lab.method.euler import Euler
from lab.method.abstract_method import AbstractMethod
from lab.method.euler_modiffed import EulerModified
from lab.method.miln import Miln
from lab.model.Equation import Equation

import matplotlib.pyplot as plt

equation_list: list[Equation] = [
    Equation(lambda x, y: y + (1 + x) * y ** 2, lambda x: - 1 / x, "y` = y + (1 + x) * y ** 2"),
    Equation(lambda x, y: 2 * x - y + x ** 2, lambda x: x ** 2, "y` = 2 * x - y + x ** 2"),
    Equation(lambda x, y: (x - y) ** 2 + 1, lambda x: x, "y` = (x - y) ** 2 + 1"),
]

solvers: list[type[AbstractMethod]] = [
    Euler,
    EulerModified,
    Miln
]


def main():
    _, equation = ask_for_equation(equation_list)
    x0 = ask_float("Введите x0 ")
    y0 = ask_float("Введите y0 ")
    xn = ask_float("Введите xn ")
    h = ask_float("Введите шаг h ")
    e = ask_float("Введите точность e ")
    print()
    for solver_class in solvers:
        try:
            solver = solver_class(x0, y0, xn, h, e, equation.equation, equation.solved)
        except Exception:
            print("Точность достигнуть не удалось!", file=sys.stderr)
        x_list, y_list = solver.solve()
        plt.plot(x_list, y_list, label=solver.name)
    x_list = np.linspace(x0, xn, 1000)
    plt.plot(x_list, equation.solved(x_list), label="Точное значение")
    plt.legend()
    plt.show()


if __name__ == '__main__':
    main()

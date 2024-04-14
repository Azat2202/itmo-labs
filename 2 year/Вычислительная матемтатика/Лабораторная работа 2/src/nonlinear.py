import math
import sys

import numpy

from ConsoleWorker import *
from methods.HalfDivision import HalfDivision
from methods.Iteration import Iteration
from methods.Secant import Secant
from models.Equation import Equation

equation_list: list[Equation] = [
    Equation(lambda x: x ** 3 + 4.81 * x ** 2 - 17.37 * x + 5.38, "x^3 + 4.81x^2  - 17.37x + 5.38"),
    Equation(lambda x: x ** 3 - 1.89 * x ** 2 - 2 * x + 1.76, "x^3 - 1.89x^2  - 2x + 1.76"),
    Equation(lambda x: numpy.sin(x) + 0.02 * x ** 2 - 1, r"\sin{x} + 0.02x^2 - 1"),
    Equation(lambda x: numpy.sqrt(x) - 0.05 * x ** 3 + 0.5 * x ** 2 - 5,
             r"\sqrt{x} - 0.05x^3 + 0.5x^2 - 5"),
]

method_list: list[type[Method]] = [
    HalfDivision,
    Secant,
    Iteration
]

if __name__ == "__main__":
    equation_index, equation = ask_for_equation(equation_list)
    if equation_index == 3:
        equation.draw(0, 20)
    else:
        equation.draw(-20, 20)

    left, right, accuracy = ask_for_initial_data()
    if equation_index == 3:
        if left < 0 or right < 0:
            print("Значения вне ОДЗ")
            sys.exit(0)
    method_index, method_type = ask_for_method(method_list)
    method = method_type(equation, left, right, accuracy)
    if not method.check():
        print("На заданном промежутке не один корень!", file=sys.stderr)
        sys.exit(0)
    method.solve()

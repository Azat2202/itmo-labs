import numpy

import ConsoleWorker
from methods.AllRectangles import AllRectangles
from methods.LeftRectangles import LeftRectangles
from methods.Method import Method
from methods.MiddleRectangles import MiddleRectangles
from methods.RightRectangles import RightRectangles
from methods.Simpson import Simpson
from methods.Trapezoid import Trapezoid
from models.Equation import Equation

equation_list: list[Equation] = [
    Equation(lambda x: numpy.sin(x) + 4, "sin(x) + 4"),
    Equation(lambda x: numpy.cos(x) - numpy.sin(x) * x, "cos(x) - x*sin(x)"),
    Equation(lambda x: x ** 3 - 1.89 * x ** 2 - 2 * x + 1.76, "x^3 - 1.89x^2  - 2x + 1.76"),
]

method_list: list[type[Method]] = [
    AllRectangles,
    Trapezoid,
    Simpson
]

if __name__ == "__main__":
    eq_index, equation = ConsoleWorker.ask_for_equation(equation_list)
    left = ConsoleWorker.ask_float("Введите левый предел интегрирования ")
    right = ConsoleWorker.ask_float("Введите правый предел интегрирования ")
    accuracy = ConsoleWorker.ask_float("Введите точность интегрирования ")
    method_index, method_type = ConsoleWorker.ask_for_method(method_list)
    method = method_type(equation, left, right, accuracy)
    method.solve()


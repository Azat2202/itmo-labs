import numpy
import sympy

from ConsoleWorker import *
from methods.System_Newton import SystemNewtonSolver
from models.SystemEquation import SystemEquation

equation_list: list[SystemEquation] = [
    SystemEquation(
        lambda x, y: sympy.sin(x + 1) - y - 1.2,
        lambda x, y: 2 * x + sympy.cos(y) - 2,
        "sin(x + 1) - y - 1.2",
        "2*x + cos(y) - 2"
    ),
    SystemEquation(
        lambda x, y: x ** 2 + y ** 2 - 4,
        lambda x, y: - 3 * x ** 2 + y,
        "x^2 + y^2 - 4",
        "y - 3x^2"
    ),
    SystemEquation(
        lambda x, y: x**2 * sympy.cos(y) + y**2 * sympy.sin(x) - 2,
        lambda x, y: x**2 + y**2 - 20,
        "x^2 * cos(y) + y^2 * sin(x) - 2",
        "x^2 + y^2 - 20"
    )
]

if __name__ == "__main__":
    equation_index, equation = ask_for_equation_system(equation_list)
    equation.draw(-10, 10, -10, 10)
    x_start, y_start, accuracy = ask_for_initial_data_system()
    solver = SystemNewtonSolver(equation, x_start, y_start, accuracy)
    solver.solve()

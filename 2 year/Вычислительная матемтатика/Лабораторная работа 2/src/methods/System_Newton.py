import sys

from prettytable import PrettyTable
from scipy.misc import derivative
import numpy as np
from sympy import symbols, diff

from models.SystemEquation import SystemEquation


# TODO: как понять когда процесс расходится
class SystemNewtonSolver:
    name = "ньютона"
    max_iter_count = 8

    def __init__(self, equation: SystemEquation, x_start: float, y_start: float, accuracy: float):
        self.x_start = x_start
        self.y_start = y_start
        self.accuracy = accuracy
        self.equation = equation
        self.symbols_after_dot = max(0, len(str(accuracy)) - 2)

    def solve(self) -> None:
        x_0 = 0  # Just to make first loop go
        y_0 = 0
        prev_x = x_0
        prev_y = y_0
        dx = self.x_start
        dy = self.y_start
        iter_count = 1
        table = PrettyTable(["N", "x", "y", "dx", "dy", "f1", "f2"])

        while (np.abs(dx) > self.accuracy or np.abs(dy) > self.accuracy) and iter_count <= self.max_iter_count:
            prev_x = x_0
            prev_y = y_0
            x_0 += dx
            y_0 += dy
            df1_dx, df1_dy, df2_dx, df2_dy = self.count_Jakobian(x_0, y_0)
            if df1_dx * df2_dy - df1_dy * df2_dx == 0:
                print("Последовательность не сходится к корню!")
                return
            left_side = np.array([[df1_dx, df1_dy], [df2_dx, df2_dy]]).astype('float64')
            right_side = (-1 * np.array([self.equation.f1(x_0, y_0), self.equation.f2(x_0, y_0)])).astype('float64')
            dx, dy = np.linalg.solve(left_side, right_side)
            table.add_row(list(map(lambda i: round(i, self.symbols_after_dot),
                                   [iter_count, x_0, y_0, dx, dy, self.equation.f1(x_0, y_0), self.equation.f2(x_0, y_0)])))
            iter_count += 1
        if iter_count >= self.max_iter_count:
            print(table)
            print(f"{self.max_iter_count} итераций не достаточно чтобы прийти к результату!", file=sys.stderr)
            return
        print(table)
        x_0 += dx
        y_0 += dy
        print(f"Вектор неизвестных: ({x_0} ; {y_0})")
        print(f"Количество итераций: {iter_count}")
        print(f"Вектор погрешностей: ({np.abs(x_0 - prev_x)} ; {np.abs(y_0 - prev_y)})")
        print(f"Проверка: ")
        print("f1 = ", self.equation.f1(x_0, y_0))
        print("f1 = ", self.equation.f2(x_0, y_0))

    def count_Jakobian(self, x_0: float, y_0: float) -> tuple[float, float, float, float]:
        x_symb, y_symb = symbols('x y')
        df1_dx = diff(self.equation.f1(x_symb, y_symb), x_symb).subs({x_symb: x_0, y_symb: y_0})
        df1_dy = diff(self.equation.f1(x_symb, y_symb), y_symb).subs({x_symb: x_0, y_symb: y_0})
        df2_dx = diff(self.equation.f2(x_symb, y_symb), x_symb).subs({x_symb: x_0, y_symb: y_0})
        df2_dy = diff(self.equation.f2(x_symb, y_symb), y_symb).subs({x_symb: x_0, y_symb: y_0})
        return df1_dx, df1_dy, df2_dx, df2_dy

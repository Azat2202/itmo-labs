from sys import stderr

import numpy
import scipy.misc
from prettytable import PrettyTable

from methods.Method import Method


class Iteration(Method):
    name = "простых итераций"

    def solve(self) -> None:
        table = PrettyTable(["N", "x", "x_next", "f(x_next)", "|x_next - x|"])
        f = self.equation.function
        f_der_left = scipy.misc.derivative(f, self.left, dx=1e-6)
        f_der_right = scipy.misc.derivative(f, self.right, dx=1e-6)
        lambda_var = 1 / max(f_der_left, f_der_right)
        if f_der_right > 0 and f_der_left > 0:
            lambda_var = - 1 * lambda_var
        phi = lambda x: x + lambda_var * f(x)
        phi_der = lambda x: scipy.misc.derivative(phi, x, dx=1e-6)
        if phi_der(self.left) >= 1 or phi_der(self.right) >= 1:
            print("Условие сходимости не выполнено(", file=stderr)
            return
        print(f"{phi_der(self.left)=} \n{phi_der(self.right)=}")

        x_prev = self.left
        x = phi(x_prev)
        iter_count = 1
        while numpy.abs(f(x)) > self.accuracy and iter_count < self.max_iter_count and x < self.right:
            table.add_row(list(map(lambda i: round(i, self.symbols_after_dot),
                              [iter_count, x_prev, x, f(x), numpy.abs(x_prev - x)])))
            x_prev = x
            x = phi(x_prev)
            iter_count += 1
        table.add_row(list(map(lambda i: round(i, self.symbols_after_dot),
                               [iter_count, x_prev, x, f(x), numpy.abs(x_prev - x)])))
        print(table)
        print(f"Найденный корень: {round(x, self.symbols_after_dot)}")
        print(f"Значение функции: {f(x)}")
        print(f"Количество итераций: {iter_count}")

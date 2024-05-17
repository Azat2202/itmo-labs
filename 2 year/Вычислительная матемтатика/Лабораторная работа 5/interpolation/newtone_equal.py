import math
from typing import Callable, List

from prettytable import PrettyTable

from data import Point
from interpolation import AbstractInterpolation


class NewtonEqual(AbstractInterpolation):
    name = "интерполяция Ньютона для равноотстоящих узлов"

    @staticmethod
    def check(table: List[Point]) -> bool:
        n = len(table)
        h = table[1].x - table[0].x
        for i in range(2, n):
            if table[i].x - table[i - 1].x != h:
                return False
        return True

    def divided_diffs(self) -> list[list[float]]:
        diff = [[0 for _ in range(self.n)] for _ in range(self.n)]
        for i in range(self.n):
            diff[i][0] = self.table[i].y
        for j in range(1, self.n):
            for i in range(self.n - j):
                diff[i][j] = diff[i + 1][j - 1] - diff[i][j - 1]
        return diff

    def print_divided_diffs(self, diff: list[list[float]]):
        table = PrettyTable()
        table.title = "Конечные разности"
        table.field_names = ["x", "y"] + [f"d{i}y" for i in range(1, self.n)]
        for i in range(len(self.table)):
            table.add_row([self.table[i].x, *map(lambda x: "%.3f" % x, diff[i])])
        print(table)

    def create_function(self) -> Callable[[float], float]:
        diff = self.divided_diffs()
        self.print_divided_diffs(diff)
        h = self.table[1].x - self.table[0].x
        t_forward = lambda x: (x - self.table[0].x) / h

        forward = lambda x: self.table[0].y + sum([
            diff[0][i] *
            math.prod([
                t_forward(x) - j
                for j in range(i)
            ]) / math.factorial(i)
            for i in range(1, self.n)
        ])

        t_backward = lambda x: (x - self.table[-1].x) / h
        backward = lambda x: self.table[-1].y + sum([
            diff[-i - 1][i] *
            math.prod([
                t_backward(x) + j
                for j in range(i)
            ]) / math.factorial(i)
            for i in range(1, self.n)
        ])

        return lambda x: forward(x) if (self.table[-1].x - self.table[0].x) / 2 < x else backward(x)

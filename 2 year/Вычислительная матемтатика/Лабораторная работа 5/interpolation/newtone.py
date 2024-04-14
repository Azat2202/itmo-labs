import math
from typing import Callable

from prettytable import PrettyTable

from interpolation import AbstractInterpolation


class Newtone(AbstractInterpolation):
    name = "интерполяция Ньютона"

    def divided_diffs(self) -> list[list[float]]:
        diff = [[0 for _ in range(self.n)] for _ in range(self.n)]
        for i in range(self.n):
            diff[i][0] = self.table[i].y
        for j in range(1, self.n):
            for i in range(self.n - j):
                diff[i][j] = (diff[i + 1][j - 1] - diff[i][j - 1]) / (self.table[i + j].x - self.table[i].x)
        return diff

    def print_diffs(self, diff: list[list[float]]):
        table = PrettyTable()
        table.title = "Разделенные разности"
        table.field_names = ["x", "y"] + [f"d{i}y" for i in range(1, self.n)]
        table.float_format = ".3"
        for i in range(len(self.table)):
            table.add_row([self.table[i].x, *diff[i]])
        print(table)

    def create_function(self) -> Callable[[float], float]:
        diff = self.divided_diffs()
        self.print_diffs(diff)
        return lambda x: diff[0][0] + sum([
            diff[0][k] * math.prod([
                x - self.table[j].x
                for j in range(k)
            ])
            for k in range(1, self.n)
        ])

import math
from typing import Callable, List

from prettytable import PrettyTable

from data import Point
from interpolation import AbstractInterpolation

# TODO: интерполирования назад
class Newton(AbstractInterpolation):
    name = "интерполяция Ньютона"

    @staticmethod
    def check(table: List[Point]) -> bool:
        n = len(table)
        h = table[1].x - table[0].x
        for i in range(2, n):
            if table[i].x - table[i - 1].x != h:
                return True
        return False

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

        return lambda x: forward(x) if (self.table[-1].x - self.table[0].x) / 2 < x else backward(x)

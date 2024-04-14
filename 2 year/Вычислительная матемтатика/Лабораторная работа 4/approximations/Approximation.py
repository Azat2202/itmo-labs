from typing import Callable, List

import numpy as np
from prettytable import PrettyTable

from approximations import Point


class Approximation:
    name = ""
    view = ""
    table: List[Point]
    n: int
    func: Callable[[float], float]
    r: float
    d: float
    a = "-"
    b = "-"
    c = "-"
    d = "-"

    def __init__(self, table: List[Point]) -> None:
        self.table = table
        self.n: int = len(table)

    def check(self) -> bool:
        """ Check if there is no two dots with same x"""
        return len(set([p.x for p in self.table])) == len(self.table)

    def solve(self, file) -> Callable[[float], float]:
        raise NotImplemented

    def report(self, file):
        out_table = PrettyTable()
        out_table.title = self.name
        out_table.field_names = ["N", "x", "y", "P", "e"]
        out_table.float_format = ".3"
        for i, point in enumerate(self.table):
            out_table.add_row([
                i,
                point.x,
                point.y,
                self.func(point.x),
                self.func(point.x) - point.y
            ])
        print(out_table, file=file)
        self.r = self.determination()
        print(f"Коэффициент детерминации: {self.r: .2f}", file=file)
        if self.r < 0.5:
            print(f"Точность аппроксимации недостаточна", file=file)
        elif self.r < 0.75:
            print(f"Слабая аппроксимация", file=file)
        elif self.r < 0.95:
            print(f"Удовлетворительная аппроксимация", file=file)
        else:
            print(f"Высокая точность аппроксимации", file=file)

    def determination(self) -> float:
        phi_ = sum([self.func(p.x) for p in self.table]) / self.n
        return 1 - (
                sum([(p.y - self.func(p.x)) ** 2 for p in self.table]) /
                sum([(p.y - phi_) ** 2 for p in self.table])
        )

    def mean_square(self) -> float:
        return np.sqrt(
            self.get_S() / self.n
        )

    def get_S(self) -> float:
        return sum([(self.func(p.x) - p.y)**2 for p in self.table])

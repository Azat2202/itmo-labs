import math
from typing import Callable

from prettytable import PrettyTable

from approximations.Approximation import Approximation


class Linear(Approximation):
    name = "Линейная аппроксимация"
    view = "ax + b"

    def solve(self, file) -> Callable[[float], float]:
        a, b = self.get_linear_ratio()
        self.func = lambda x: a * x + b
        self.report(file=file)
        self.a, self.b = a, b
        return self.func

    def get_linear_ratio(self) -> tuple[float, float]:
        SX: float = sum([p.x for p in self.table])
        SXX: float = sum([p.x ** 2 for p in self.table])
        SY: float = sum([p.y for p in self.table])
        SXY: float = sum([p.x * p.y for p in self.table])

        delta = SXX * self.n - SX * SX
        delta1 = SXY * self.n - SX * SY
        delta2 = SXX * SY - SX * SXY

        a = delta1 / delta
        b = delta2 / delta
        return a, b

    def report(self, file):
        super().report(file)
        pirson = self.pirson()
        print(f"Коэффициент корреляции Пирсона: {pirson: .2f}", file=file)
        if pirson < 0.3:
            print("Связь слабая", file=file)
        elif pirson < 0.5:
            print("Связь умеренная", file=file)
        elif pirson < 0.7:
            print("Связь заметная", file=file)
        elif pirson < 0.9:
            print("Связь высокая", file=file)
        else:
            print("Связь весьма высокая", file=file)

    def pirson(self) -> float:
        x_ = sum([p.x for p in self.table]) / self.n
        y_ = sum([p.y for p in self.table]) / self.n
        return (
                sum([(p.x - x_) * (p.y - y_) for p in self.table])
                /
                math.sqrt(
                    sum([(p.x - x_) ** 2 for p in self.table]) *
                    sum([(p.y - y_) ** 2 for p in self.table])
                ))

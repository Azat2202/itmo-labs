from typing import List, Callable
from numpy import linspace
import matplotlib.pyplot as plt

from data import Point


class GraphPrinter:
    dimension = 1000

    def __init__(self, table: List[Point], functions: List[Callable[[float], float]], labels: List[str]):
        self.table = table
        self.a = min([x for x, y in table])
        self.b = max([x for x, y in table])
        self.a -= 1
        self.b += 1
        self.functions = functions
        self.labels = labels

    def show(self):
        x = linspace(self.a, self.b, self.dimension)
        y = list()
        for f in self.functions:
            y.append([f(i) for i in x])
        for p in self.table:
            plt.plot(p.x, p.y, 'ro')
        for i, y_ in enumerate(y):
            plt.plot(x, y_, label=self.labels[i])
        plt.legend()
        plt.show()

import sys
from typing import List

import numpy as np


class Gauss:
    def __init__(self, left: List[List[float]], right: List[float]):
        self.left = left
        self.right = right
        self.n = len(self.right)

    def solve(self) -> List[float]:
        self.forward()
        return self.backward()

    def solve_(self):
        n = self.n
        a = np.zeros((self.n, self.n + 1))
        x = np.zeros(self.n)

        table = self.left
        for i, el in enumerate(self.right):
            table[i].append(el)
        a = np.array(table)
        print(a)

        for i in range(self.n):
            if a[i][i] == 0.0:
                sys.exit('Divide by zero detected!')

            for j in range(i + 1, self.n):
                ratio = a[j][i] / a[i][i]

                for k in range(self.n + 1):
                    a[j][k] = a[j][k] - ratio * a[i][k]
        print(a)

        x[n - 1] = a[n - 1][n] / a[n - 1][n - 1]

        for i in range(n - 2, -1, -1):
            x[i] = a[i][n]

            for j in range(i + 1, n):
                x[i] = x[i] - a[i][j] * x[j]

            x[i] = x[i] / a[i][i]
        print(x)
        return x

    def forward(self):
        for i in range(self.n):
            for j in range(i + 1, self.n):
                ratio = self.left[j][i] / self.left[i][i]
                self.right[j] -= ratio * self.right[i]
                for k in range(self.n):
                    self.left[j][k] -= ratio * self.left[i][k]

    def backward(self) -> List[float]:
        x = [0 for _ in range(self.n)]
        x[-1] = self.right[-1] / self.left[-1][-1]
        for i in range(self.n - 2, -1, -1):
            x[i] = (self.right[i] - sum([
                self.left[i][k] * x[k]
                for k in range(i + 1, self.n)
            ])) / self.left[i][i]
        return x

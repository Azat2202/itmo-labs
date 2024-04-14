from typing import Callable
import matplotlib.pyplot as plt
import numpy as np

dx = 0.00001


class Equation:
    def __init__(self, function: Callable[[float], float], text: str):
        self.text = text
        self.function = function

    def draw(self, left: float, right: float):
        x = np.linspace(left, right, 100000)
        func = np.vectorize(self.function)(x)

        plt.title = 'График функции'
        plt.grid(True, which='both')
        plt.xlabel('X')
        plt.ylabel('Y')
        plt.axhline(y=0, color='gray', label='$y = 0$')
        plt.plot(x, func, 'blue', label=f"${self.text}$")
        plt.legend()
        plt.savefig('graph.png')
        plt.show()

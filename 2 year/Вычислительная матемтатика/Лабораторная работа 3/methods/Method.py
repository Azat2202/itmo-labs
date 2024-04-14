from models.Equation import Equation


class Method:
    name = None
    max_iter_count = 500
    n = 4
    k = 2

    def __init__(self, equation: Equation, left: float, right: float, accuracy: float) -> object:
        self.accuracy = accuracy
        self.right = right
        self.left = left
        self.equation = equation
        self.symbols_after_dot = max(0, len(str(accuracy)) - 2)
        self.runge = lambda I_h, I_h_2: (I_h_2 - I_h) / (2 ** self.k - 1)

    def solve(self) -> None:
        pass


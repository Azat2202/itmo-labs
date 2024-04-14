from models.Equation import Equation


class Method:
    name = None
    max_iter_count = 500

    def __init__(self, equation: Equation, left: float, right: float, accuracy: float):
        self.accuracy = accuracy
        self.right = right
        self.left = left
        self.equation = equation
        self.symbols_after_dot = max(0, len(str(accuracy)) - 2)

    def solve(self) -> None:
        pass

    def check(self) -> bool:
        if not self.equation.root_exists(self.left, self.right):
            return False
        return True

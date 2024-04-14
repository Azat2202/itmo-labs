from prettytable import PrettyTable

from methods.LeftRectangles import LeftRectangles
from methods.Method import Method
from methods.MiddleRectangles import MiddleRectangles
from methods.RightRectangles import RightRectangles


class AllRectangles(Method):
    name = "метод прямоугольников"

    def solve(self) -> None:
        LeftRectangles(self.equation, self.left, self.right, self.accuracy).solve()
        MiddleRectangles(self.equation, self.left, self.right, self.accuracy).solve()
        RightRectangles(self.equation, self.left, self.right, self.accuracy).solve()

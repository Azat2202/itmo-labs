from prettytable import PrettyTable

from methods.Method import Method


class MiddleRectangles(Method):
    name = "метод средних прямоугольников"
    k=2

    def solve(self) -> None:
        table = PrettyTable(["N", "n", "I", "runge"])
        table.title = self.name.capitalize()
        f = self.equation.function
        h = (self.right - self.left) / self.n
        x_linspace = [self.left + h / 2 + h * i for i in range(self.n)]
        f_linspace = [f(i) for i in x_linspace]
        I_prev = 10000
        I = sum([y * h for y in f_linspace])
        self.n *= 2
        iter_count = 2
        while abs(self.runge(I, I_prev)) > self.accuracy:
            h = (self.right - self.left) / self.n
            x_linspace = [self.left + h / 2 * i for i in range(1, self.n + 1)]
            f_linspace = [f(i) for i in x_linspace]
            I_prev = I
            I = sum([y * h for y in f_linspace])
            table.add_row([iter_count, self.n, I, self.runge(I, I_prev)])
            self.n *= 2
            iter_count += 1
        self.n //= 2
        print(table)
        print("Значение интеграла: ", I)
        print("Число разбиений:", self.n)

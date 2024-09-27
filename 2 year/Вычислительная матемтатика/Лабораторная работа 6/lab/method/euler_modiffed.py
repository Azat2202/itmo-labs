from prettytable import PrettyTable

from lab.method.abstract_method import AbstractMethod


class EulerModified(AbstractMethod):
    p=2
    name = "Модифицированный Метод Эйлера"

    def solve(self) -> [list[float], list[float]]:
        y, table, x_list, y_list = self.perform_euler()
        while True:
            self.h /= 2
            y_new, table_new, x_list_new, y_list_new = self.perform_euler()
            runge = abs(y - y_new) / (2 ** self.p - 1)
            if runge <= self.e:
                print(table)
                print(f"Погрешность по правилу Рунге: {runge : .5f}")
                return x_list, y_list
            y = y_new
            table = table_new
            x_list = x_list_new
            y_list = y_list_new

    def perform_euler(self) -> [float, PrettyTable, list[float], list[float]]:
        table = PrettyTable()
        table.title = self.name
        table.field_names = ["i", "xi", "yi", "f(xi, yi)", "Точное решение"]
        table.float_format = ".3"
        x = self.x0
        y = self.y0
        y_list = [y]
        x_list = [x]
        i = 0
        while x < self.xn:
            table.add_row([i, x, y, self.f(x, y), self.f_ac(x)])
            y += self.h / 2 * (self.f(x, y) + self.f(x + self.h, y + self.h * self.f(x, y)))
            x += self.h
            x_list.append(x)
            y_list.append(y)
            i += 1
        # print(*[f"{i : .3f}" for i in x_list])
        # print(*[f"{i : .3f}" for i in y_list])
        return y, table, x_list, y_list

from prettytable import PrettyTable

from lab.method.abstract_method import AbstractMethod


class Miln(AbstractMethod):
    p=4
    name = "Метод Милна"

    def solve(self) -> [list[float], list[float]]:
        while True:
            eps_max, table, x_list, y_list = self.perform_miln()
            self.h /= 2
            # if eps_max <= self.e:
            print(table)
            print(f"Оценка точности: {eps_max : .5f}")
            return x_list, y_list

    def perform_miln(self) -> [float, PrettyTable, list[float], list[float]]:
        table = PrettyTable()
        table.title = self.name
        table.field_names = ["i", "xi", "yi", "f(xi, yi)", "Точное решение", "e"]
        table.float_format = ".5"
        eps_max = 0.0
        x = self.x0
        f = self.f
        h = self.h
        y = self.y0
        x_list = [x]
        y_list = [y]
        f_list = [f(x, y)]
        for i in range(3):
            y += h * self.f(x, y)
            x += h
            x_list.append(x)
            y_list.append(y)
            f_list.append(f(x, y))
        # print(x_list)
        # print(y_list)
        # print(f_list)
        for i, x in enumerate(x_list):
            table.add_row([i, x, y_list[i], self.f(x, y), self.f_ac(x), abs(y - self.f_ac(x))])
        i = 0
        while x < self.xn:
            x += self.h
            y = y_list[-4] + 4 * h / 3 * (2 * f_list[-3] - f_list[-2] + 2 * f_list[-1])
            new_y_corrected = y_list[-2] + h / 3 * (f_list[-2] + 4 * f_list[-1] + self.f(x, y))
            table.add_row([i + 3, x, new_y_corrected, self.f(x, y), self.f_ac(x), abs(new_y_corrected - self.f_ac(x))])
            while abs(y - new_y_corrected) > self.e:
                y = new_y_corrected
                new_y_corrected = y_list[-2] + h / 3 * (f_list[-2] + 4 * f_list[-1] + self.f(x, y))
                table.add_row(["", "", new_y_corrected, self.f(x, y), self.f_ac(x), abs(new_y_corrected - self.f_ac(x))])
            print(abs(y - new_y_corrected))
            y = new_y_corrected
            x_list.append(x)
            y_list.append(y)
            f_list.append(self.f(x, y))
            eps_max = max(eps_max, abs(y - self.f_ac(x)))
            i += 1
        # print(*[f"{i : .3f}" for i in x_list])
        # print(*[f"{i : .3f}" for i in y_list])
        return eps_max, table, x_list, y_list

from prettytable import PrettyTable

from lab.method.abstract_method import AbstractMethod


class Miln(AbstractMethod):
    p=4
    name = "Метод Милна"

    def solve(self) -> [list[float], list[float]]:
        while True:
            eps_max, table, x_list, y_list = self.perform_milt()
            self.h /= 2
            print(eps_max)
            if eps_max <= self.e:
                print(table)
                return x_list, y_list

    def perform_milt(self) -> [float, PrettyTable, list[float], list[float]]:
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
            k1 = h * f(x, y)
            k2 = h * f(x + h / 2, y + k1 / 2)
            k3 = h * f(x + h / 2, y + k2 / 2)
            k4 = h * f(x + h, y + k3)
            y = y + (k1 + 2 * k2 + 2 * k3 + k4) / 6
            f_list.append(f(x, y))
            x_list.append(x)
            y_list.append(y)
            x += h
        i = 0
        while x < self.xn + self.h:
            y = y_list[-4] + 4 * h / 3 * (2 * f_list[-3] - f_list[-2] + 2 * f_list[-1])
            new_y_corrected = y_list[-2] + h / 3 * (f_list[-2] + 4 * f_list[-1] + self.f(x, y))
            table.add_row([i + 3, x, new_y_corrected, self.f(x, y), self.f_ac(x), abs(y - self.f_ac(x))])
            while abs(y - new_y_corrected) > self.e:
                y = new_y_corrected
                new_y_corrected = y_list[-2] + h / 3 * (f_list[-2] + 4 * f_list[-1] + self.f(x, y))
                table.add_row(["", "", new_y_corrected, self.f(x, y), self.f_ac(x), abs(new_y_corrected - self.f_ac(x))])
            y = new_y_corrected
            x_list.append(x)
            y_list.append(y)
            f_list.append(self.f(x, y))
            eps_max = max(eps_max, abs(y - self.f_ac(x)))
            y += self.h * self.f(x, y)
            x += self.h
            i += 1
        return eps_max, table, x_list, y_list

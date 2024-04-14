import sys

"""
ВЫЧМАТ
Лабораторная работа 1
Сиразетдинов Азат Ниязович
P3216 368796
Вариант 13
Метод простых итераций
"""


class IterationMethod:
    accuracy: float = 0.000001
    size: int = 0
    matrix: list[list[float]] = []
    right_parts: list[float] = []
    MAX_ITERATION_COUNT = 1000

    def __init__(self, left: list[list[float]], right: list[float]):
            self.matrix = left
            self.right_parts = right
            self.size = len(self.right_parts)

    def solve(self) -> list[float]:
        if not self.is_diag_dominance():
            self.to_diag_dominance()
            if not self.is_diag_dominance():
                print("Приведение к матрице преобладания диагональных коэффициентов невозможно. Ответ не гарантирован!",
                      file=sys.stderr)
        X, iter_count, acc_list = self.compute()
        return X

    def to_diag_dominance(self) -> None:
        """
        Будем рассматривать максимальный элемент в строке и столбце.
        Если он находится в строке, то перемещаем столбцы
        Если он находится в столбце, то перемещаем строки
        """
        for i in range(self.size):
            max_line = max(map(abs, self.matrix[i][i:]))
            max_column = max(map(abs, self.get_column(i)[i:]))
            if max_column >= max_line:
                line_index = self.get_column(i)[i:].index(max_column) + i
                self.matrix[i], self.matrix[line_index] = self.matrix[line_index], self.matrix[i]
                self.right_parts[i], self.right_parts[line_index] = self.right_parts[line_index], self.right_parts[i]
            else:
                column_index = self.matrix[i].index(max_line)
                column_buffer = self.get_column(column_index)
                self.set_column(column_index, self.get_column(i))
                self.set_column(i, column_buffer)

    def is_diag_dominance(self) -> bool:
        is_strictly = False
        for i in range(self.size):
            if self.matrix[i][i] > sum(map(abs, self.matrix[i])) - abs(self.matrix[i][i]):
                is_strictly = True
            elif self.matrix[i][i] < sum(map(abs, self.matrix[i])) - abs(self.matrix[i][i]):
                return False
        return is_strictly

    def compute(self) -> (list[float], int, list[float]):
        C = [[-1 * num / line[i] if i != j else 0
              for j, num in enumerate(line)
              ]
             for i, line in enumerate(self.matrix)
             ]
        D = [num / self.matrix[i][i] for i, num in enumerate(self.right_parts)]
        X = D.copy()
        # print(C)
        # print(D)
        iter_count = 0
        while True:
            # print("Итерация ", iter_count)
            # print("Значения X: ", X)
            iter_count += 1
            if iter_count > self.MAX_ITERATION_COUNT:
                print("Превышено максимальное количество итераций!", file=sys.stderr)
                sys.exit(1)
            X_next = [
                sum(self.mul_vectors(C[i], X)) + D[i]
                for i in range(self.size)
            ]
            if max(map(abs, self.sub_vectors(X_next, X))) < self.accuracy:
                r = []
                for i in range(len(self.matrix)):
                    r.append(sum(self.mul_vectors(self.matrix[i], X)) - self.right_parts[i])
                print("Критерий по невязке:")
                print(r)
                return X_next, iter_count, list(map(abs, self.sub_vectors(X_next, X)))
            X = X_next

    def get_column(self, i: int) -> list[float]:
        return [j[i] for j in self.matrix]

    def set_column(self, i: int, column: list[float]):
        for j in range(self.size):
            self.matrix[j][i] = column[j]

    def mul_vectors(self, list1: list[float], list2: list[float]) -> list[float]:
        return [list1[i] * list2[i] for i in range(len(list1))]

    def sub_vectors(self, list1: list[float], list2: list[float]):
        return [list1[i] - list2[i] for i in range(len(list1))]

    def print(self):
        for i, row in enumerate(self.matrix):
            for element in row:
                print("{:8}".format(element), end='')
            print("{:8}".format(self.right_parts[i]))

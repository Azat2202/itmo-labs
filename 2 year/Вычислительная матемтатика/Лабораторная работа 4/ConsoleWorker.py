import sys
from io import TextIOWrapper
from typing import Union, List

from approximations.Point import Point


# TODO: check if two dots of one x

class ConsoleWorker:
    is_input_from_file: bool
    is_output_to_file: bool
    input_file = Union[None, TextIOWrapper]
    output_file = Union[None, TextIOWrapper]
    table: List[Point] = list()

    def __init__(self):
        self.ask_input()
        self.ask_output()

    def ask_input(self):
        while True:
            line = input("Вы хотите ввести данные из файла? y/n ").strip()
            if line in ['y', 'n']:
                self.is_input_from_file = line == 'y'
                break
            else:
                print("Ввод не распознан")
        if self.is_input_from_file:
            file_name = input("Введите название файла ")
            try:
                self.input_file = open(file_name)
            except FileNotFoundError:
                print("Файл не найден!", file=sys.stderr)
                exit(1)
        else:
            self.input_file = sys.stdin

    def ask_output(self):
        while True:
            line = input("Вы хотите выводить данные в файл? y/n ").strip()
            if line in ['y', 'n']:
                self.is_output_to_file = line == 'y'
                break
            else:
                print("Ввод не распознан")
        if self.is_output_to_file:
            file_name = input("Введите название файла ")
            self.output_file = open(file_name, "w", encoding="utf-8")
        else:
            self.output_file = sys.stdout

    def close_files(self):
        if self.is_input_from_file:
            self.input_file.close()
        if self.is_output_to_file:
            self.output_file.close()

    def ask_table(self):
        self.__read_table_from_file() if self.is_input_from_file else self.__read_table_from_stdin()

    def __read_table_from_file(self):
        try:
            for line in self.input_file.readlines():
                x, y = map(float, line.strip().split())
                self.table.append(Point(x=x, y=y))
        except ValueError:
            print("Файл не валиден!", file=sys.stderr)
            exit(1)

    def __read_table_from_stdin(self):
        line = input("Вводите точки в формате \'x y\'\n")
        while line.strip():
            try:
                x, y = map(float, line.strip().split())
                self.table.append(Point(x=x, y=y))
            except ValueError:
                print("Данные не валидны!")
            finally:
                line = input()

    def ask_int(question: str) -> int:
        num: int
        while True:
            try:
                num = int(input(question))
                return num
            except ValueError:
                print("Номер должен быть числом")

    def ask_float(question: str) -> float:
        num: float
        while True:
            try:
                num = float(input(question))
                return num
            except ValueError:
                print("Номер должен быть числом")

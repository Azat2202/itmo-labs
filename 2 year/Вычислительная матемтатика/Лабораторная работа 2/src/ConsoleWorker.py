from typing import Tuple

from methods.Method import Method
from models.Equation import Equation
from models.SystemEquation import SystemEquation


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


def ask_for_equation(equations: list[Equation]) -> tuple[int, Equation]:
    number: int
    while True:
        print(f"Выберите уравнение:")
        print("\n".join(f"{i}: {eq.text}" for i, eq in enumerate(equations)))
        number = ask_int("")
        if number not in range(len(equations)):
            print("Номер уравнения должен быть из списка")
        else:
            return number, equations[number]


def ask_for_equation_system(equations: list[SystemEquation]) -> tuple[int, SystemEquation]:
    number: int
    while True:
        print(f"Выберите уравнение:")
        print("\n".join(f"{i}: \n/ {eq.text1} \n\\ {eq.text2}" for i, eq in enumerate(equations)))
        number = ask_int("")
        if number not in range(len(equations)):
            print("Номер уравнения должен быть из списка")
        else:
            return number, equations[number]


def ask_for_method(method_list: list[type[Method]]) -> tuple[int, type[Method]]:
    method: type[Method]
    while True:
        print(f"Выберите метод:")
        print("\n".join(f"{i}: {method.name}" for i, method in enumerate(method_list)))
        method_index = ask_int("")
        if method_index not in range(len(method_list)):
            print("Номер уравнения должен быть из списка")
        else:
            return method_index, method_list[method_index]


def ask_for_initial_data() -> tuple[float, float, float]:
    left: float
    right: float
    accuracy: float

    while True:
        line = input("Вы хотите ввести данные из файла? [y/n] ").strip()
        if line in ["y", "n"]:
            is_file_input = line == "y"
            break
    if is_file_input:
        left, right, accuracy = ask_for_initial_data_from_file()
    else:
        left, right, accuracy = ask_for_initial_data_from_console()
    return left, right, accuracy


def ask_for_initial_data_from_file() -> tuple[float, float, float]:
    left: float
    right: float
    accuracy: float
    exp_heading = "left,right,accuracy"

    class WrongFormat(Exception):
        ...

    while True:
        filename = input("Введите название файла ")
        try:
            with open(filename, "r") as input_file:
                heading = input_file.readline().strip()
                if heading != exp_heading:
                    raise WrongFormat
                data = list(map(float, input_file.readline().split(",")))
                if len(data) != 3:
                    raise WrongFormat
                left, right, accuracy = data
                return left, right, accuracy
        except FileNotFoundError:
            print("Такого файла не существует!")
        except WrongFormat:
            print("Файл должен быть валидным csv файлом с заголовком: " + exp_heading)
        except ValueError:
            print("В файле должны быть числа!")


def ask_for_initial_data_from_console() -> tuple[float, float, float]:
    left = ask_float("Введите левую границу ")
    right = ask_float("Введите правую границу ")
    accuracy = ask_float("Введите точность ")
    return left, right, accuracy


def ask_for_initial_data_system() -> tuple[float, float, float]:
    x_start: float
    y_start: float
    accuracy: float

    while True:
        line = input("Вы хотите ввести данные из файла? [y/n] ").strip()
        if line in ["y", "n"]:
            is_file_input = line == "y"
            break
    if is_file_input:
        x_start, y_start, accuracy = ask_for_initial_data_from_file_system()
    else:
        x_start, y_start, accuracy = ask_for_initial_data_from_console_system()
    return x_start, y_start, accuracy


def ask_for_initial_data_from_file_system() -> tuple[float, float, float]:
    exp_heading = "x_start,y_start,accuracy"

    class WrongFormat(Exception):
        ...

    while True:
        filename = input("Введите название файла ")
        try:
            with open(filename, "r") as input_file:
                heading = input_file.readline().strip()
                if heading != exp_heading:
                    raise WrongFormat
                data = list(map(float, input_file.readline().split(",")))
                if len(data) != 3:
                    raise WrongFormat
                x_start, y_start, accuracy = data
                return x_start, y_start, accuracy
        except FileNotFoundError:
            print("Такого файла не существует!")
        except WrongFormat:
            print("Файл должен быть валидным csv файлом с заголовком: " + exp_heading)
        except ValueError:
            print("В файле должны быть числа!")


def ask_for_initial_data_from_console_system() -> tuple[float, float, float]:
    x_start = ask_float("Введите приближение по X ")
    y_start = ask_float("Введите приближение по Y ")
    accuracy = ask_float("Введите точность ")
    return x_start, y_start, accuracy

from methods.Method import Method
from models.Equation import Equation


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

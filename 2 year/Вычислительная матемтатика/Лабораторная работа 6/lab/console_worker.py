from lab.model.Equation import Equation


def ask_for_equation(equation_list: list[Equation]) -> [int, Equation]:
    while True:
        try:
            print("Выберите уравнение:")
            print("\n".join(f"{i}) " + str(equation_list[i]) for i in range(len(equation_list))))
            i = int(input())
            return i, equation_list[i]
        except ValueError:
            print("Ввод не распознан")
        except IndexError:
            print("Такой функции нет в списке")


def ask_float(question: str) -> float:
    num: float
    while True:
        try:
            num = float(input(question))
            return num
        except ValueError:
            print("Значение должно быть числом")

from typing import List

from input import AbstractInput


def chooseInput(input_list: List[type[AbstractInput]]) -> type[AbstractInput]:
    while True:
        try:
            print("Выберите вид ввода:")
            print("".join(f"{i}: {inp.name}\n" for i, inp in enumerate(input_list)), end="")
            i = int(input())
            return input_list[i]
        except (ValueError, IndexError):
            print("Такого номера нет в списке")


def askFloat(question: str) -> float:
    num: float
    while True:
        try:
            num = float(input(question))
            return num
        except ValueError:
            print("Значение должно быть числом")

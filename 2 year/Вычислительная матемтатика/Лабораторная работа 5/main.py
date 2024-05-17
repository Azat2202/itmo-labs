from consoleWorker import *
from graphPrinter import GraphPrinter
from input import *
from interpolation import *

input_variants: List[type[AbstractInput]] = [
    FileInput,
    FunctionInput,
    StandartInput
]

interpolation_variants: List[type[AbstractInterpolation]] = [
    Lagrange,
    Newton,
    NewtonEqual
]

if __name__ == '__main__':
    input_type: type[AbstractInput] = chooseInput(input_variants)
    input_ = input_type()
    table = input_.read()
    input_x = askFloat("Введите значение аргумента для интерполяции ")

    interpolations = [interp(table) for interp in interpolation_variants if interp.check(table)]
    functions = [i.f for i in interpolations]
    names = [i.name for i in interpolations]
    for interp in interpolations:
        print(f"Приближенное значение функции в точке {input_x} методом {interp.name} = {interp.at(input_x)}")

    graphPrinter = GraphPrinter(table, functions, names)
    graphPrinter.show()

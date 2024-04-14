import warnings

from prettytable import PrettyTable

from ConsoleWorker import ConsoleWorker
from GraphWorker import Graph
from approximations.Exponential import Exponential
from approximations.Linear import Linear
from approximations.Logarithmic import Logarithmic
from approximations.Power import Power
from approximations.Quadratic import Quadratic
from approximations.Cubic import Cubic
from approximations.others import *

approximations = [
    Linear,
    Quadratic,
    Cubic,
    Exponential,
    Power,
    Logarithmic,
    Third,
    Fourth,
    Fifth,
    Sixth
]
labels = [a.name for a in approximations]

if __name__ == '__main__':
    warnings.filterwarnings('ignore')  # Supress all numpy warnings

    consoleWorker = ConsoleWorker()
    consoleWorker.ask_table()

    functions = list()
    resultTable = PrettyTable()
    resultTable.title = "Выбор аппроксимирующей функции"
    resultTable.field_names = ["Вид функции", "a", "b", "c", "d", "S", "delta", "R^2"]
    resultTable.float_format = ".3"
    min_delta = None
    best_method_name = ""
    for approx in approximations:
        appr = approx(consoleWorker.table)
        if not appr.check():
            continue
        functions.append(appr.solve(consoleWorker.output_file))
        resultTable.add_row([
            appr.view,
            appr.a,
            appr.b,
            appr.c,
            appr.d,
            appr.get_S(),
            appr.mean_square(),
            appr.determination()
        ])
        if min_delta is None or appr.mean_square() < min_delta:
            min_delta = appr.mean_square()
            best_method_name = appr.name


    print(resultTable, file=consoleWorker.output_file)
    print("Наилучшая аппроксимирующая функция: ", best_method_name, file=consoleWorker.output_file)

    graphWorker = Graph(consoleWorker.table, functions, labels)
    graphWorker.show()

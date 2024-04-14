from typing import List

from data import Point
from input import AbstractInput


class FileInput(AbstractInput):
    name = "сформированные в таблице данные"

    def read(self) -> list[Point]:
        out: List[Point]
        while True:
            try:
                line = input("Введите название файла ")
                with open(line, 'r') as input_file:
                    out = [
                        Point(*map(float, i.strip().split(" ")))
                        for i in input_file.readlines()
                    ]
                    break
            except FileNotFoundError:
                print("Такого файла не найдено!")
            except (ValueError, TypeError):
                print("В файле невалидные данные!")
        return out

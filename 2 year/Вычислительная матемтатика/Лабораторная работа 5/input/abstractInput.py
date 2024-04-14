from data import *


class AbstractInput:
    name = ""

    def read(self) -> list[Point]:
        raise NotImplementedError

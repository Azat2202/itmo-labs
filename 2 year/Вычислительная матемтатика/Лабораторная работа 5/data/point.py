from dataclasses import dataclass


@dataclass
class Point:
    x: float
    y: float

    def __iter__(self):
        return iter((self.x, self.y))

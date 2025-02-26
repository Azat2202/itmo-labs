def sum_of_5_pows(i: int) -> int:
    res = 0
    while i > 0:
        res += (i % 10) ** 5 
        i //= 10
    return res


def prob2() -> int:
    res = 0
    for i in range(2, 7*9*9*9*9*9*9 + 1):
        if i == sum_of_5_pows(i):
            res += i
    return res
        


def main():
    print(prob2())


if __name__ == "__main__":
    main()

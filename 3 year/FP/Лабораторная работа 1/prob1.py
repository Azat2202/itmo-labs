def prob1(n: int) -> int:
    return sum([ i 
                for i in range(1, n + 1)
                if (i % 3 == 0) or (i % 5 == 0)
        ])



def main():
    print(prob1(999))


if __name__ == "__main__":
    main()

# глаза :
# нос -
# рот )
def count_smiles(line):
    import re
    return len(re.findall(r':-\)', line))


tests = [
    ':-):-):-):-):-):-)',
    ':-)::-))-::-)--:-):::-))):-',
    '::-:))-:)',
    ':-)8):)=)1:-)',
    ')-:'
]
if __name__ == '__main__':
    for test in tests:
        print(test, ' - ', count_smiles(test))

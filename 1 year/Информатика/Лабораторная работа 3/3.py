def find_o(line):
    import re
    pattern = r'''\b([йцкнгшщзхъждлрпвфчсмтьбЙЦКНГШЩЗХЪФВПРЛДЖЧСМТБ]*([аеёиоуэюяАЕЁИОУЭЮЯ])(?:\2*[йцкнгшщзхъждлрпвфчсмтьбЙЦКНГШЩЗХЪФВПРЛДЖЧСМТБ]*)*)\b'''
    out = [i[0] for i in re.findall(pattern, test)]
    out.sort(key=lambda l: len(l))
    return out


tests = [
    'Классное слово – обороноспособность, которое должно идти после слов: трава и молоко.',
    'Семья Блохиных занималась спиртовым ремеслом и владела 16 питейными домами в Уфе. ',
    'После смерти матери в 1871 году Николай Кондратьевич получает в наследство большое типографское дело, а его брат, Александр Кондратьевич, получает заводы по производству алкогольной продукции, который впоследствии будет распродан на аукционе из-за несостоятельности Александра Кондратьевича.',
    'Доход от книгоиздательства и книготорговли был немалым. На углу Центральной и Казанской улиц Блохин владел лавкой колониальных вин, где впоследствии в марте 1874 года открывает типографию – Печатню Блохина. ',
    'фельдъегерь, обороноспособность, длинноного, нефтепрвд'
]
if __name__ == '__main__':
    for test in tests:
        print(test, ' - ', find_o(test))

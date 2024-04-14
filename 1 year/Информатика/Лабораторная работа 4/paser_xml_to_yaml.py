import time


class XmlParser:
    out = ''

    def __init__(self, xml_str):
        self.xml_doc = ''.join([i.strip() for i in xml_str.split('\n')])

    def to_jaml(self):
        self.out = ''
        self.__find_sub(self.xml_doc[0])
        return self.out

    def __find_sub(self, line: str, tab_count=0, needs_minus=False):
        tag, info, rest = self.__match_tag(line)
        info_has_tags, rest_has_tags = self.__has_tags(info), self.__has_tags(rest)
        if not needs_minus:
            self.out += '  ' * tab_count
        else:
            self.out += '  ' * (tab_count - 2) + '  - '
        if not info_has_tags and not rest_has_tags:
            self.out += tag + ': ' + info + '\n'
        if not info_has_tags and rest_has_tags:
            if self.__get_tag(rest) == tag:
                self.out += tag + ': \n'
                for i in self.__get_list(line, tag):
                    self.out += '  ' * tab_count + '  - ' + i + '\n'
                return
            self.out += tag + ': ' + info + '\n'
            self.__find_sub(rest, tab_count)
        if info_has_tags and not rest_has_tags:
            self.out += tag + ':\n'
            self.__find_sub(info, tab_count + 1)
        if info_has_tags and rest_has_tags:
            if self.__get_tag(rest) == tag:
                self.out += tag + ':\n'
                for element in self.__get_list(line, tag):
                    self.__find_sub(element, tab_count + 2, needs_minus=True)
                return
            self.out += tag + ':\n'
            self.__find_sub(info, tab_count + 1)
            self.__find_sub(rest, tab_count)

    @staticmethod
    def __get_list(lines:str, tag:str) -> list[str]:
        # You gonna ask me wtf is here?
        # Dont ask me anything i am pythonist, and that line is working great
        # Is it fast enough? Oh, its lightning fast
        return [i[:-(len(tag) + 3)] for i in lines.split(f'<{tag}>')[1:]]

    @staticmethod
    def __has_tags(lines:str) -> bool:
        return '<' in lines and '>' in lines

    @staticmethod
    def __get_tag(lines: str) -> str:
        tag = lines[lines.find('<') + 1:lines.find('>')]
        return tag

    @staticmethod
    def __match_tag(lines:str) -> tuple[str, str, str]:
        tag = lines[lines.find('<') + 1:lines.find('>')]
        info = lines[len(tag) + 2:lines.find(f'</{tag}>')]
        rest = lines[lines.find(f'</{tag}>') + len(tag) + 3:]
        return tag, info, rest


if __name__ == '__main__':
    start_time = time.time()
    with open("Myxml.xml", "r", encoding="utf16") as input_file:
        parser_xml = XmlParser(input_file.read())
        with open('Myyaml.yaml', 'w', encoding="utf-16") as out_file:
            out_file.write(parser_xml.to_jaml())
    print('Время стократного выполнения программы: ', time.time() - start_time)

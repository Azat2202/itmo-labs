import re


class XMLParser:
    def __init__(self, xml_str, tag, main_tag):
        self.split_tag = tag
        self.xml_doc = ''.join([i.strip() for i in xml_str.split('\n')])
        self.xml_doc = self.xml_doc.strip(f'<{main_tag}>')
        self.xml_doc = self.xml_doc.strip(f'</{main_tag}>')
        # I DONT KNOW WHY IT IS LIKE THAT BUT ITS WORKING
        self.xml_doc = f'<{self.xml_doc}>'
        self.splitted_xml_doc = self.__split_list(self.xml_doc, self.split_tag)

    def to_csv(self):
        self.out = ''
        self.__find_all_tags(self.splitted_xml_doc[0])
        for info in self.splitted_xml_doc:
            self.out += '\n'
            self.__new_line(info, start_symbol='')
        return self.out

    def __new_line(self, line:str, start_symbol=','):
        tag, info, rest = self.__match_tag(line)
        info_has_tags, rest_has_tags = self.__has_tags(info), self.__has_tags(rest)
        self.out += start_symbol
        if not info_has_tags and not rest_has_tags:
            self.out += info
        elif info_has_tags and not rest_has_tags:
            self.__new_line(info, start_symbol='')
        elif not info_has_tags and rest_has_tags:
            if self.__get_tag(rest) == tag:
                for i in self.__get_list(line, tag):
                    self.out += i + ';'
            else:
                self.out += info
                self.__new_line(rest)
        elif info_has_tags and rest_has_tags:
            self.__new_line(info, start_symbol='')
            self.__new_line(rest)

    def __find_all_tags(self, line: str):
        tag, info, rest = self.__match_tag(line)
        info_has_tags, rest_has_tags = self.__has_tags(info), self.__has_tags(rest)
        if not info_has_tags and not rest_has_tags:
            self.out += ',' + tag
        if info_has_tags and not rest_has_tags:
            self.__find_all_tags(info)
        if not info_has_tags and rest_has_tags:
            self.out += ',' + tag
            self.__find_all_tags(rest)
        if info_has_tags and rest_has_tags:
            self.__find_all_tags(info)
            self.__find_all_tags(rest)
        if self.out[0] == ',':
            self.out = self.out[1:]

    @staticmethod
    def __has_tags(lines:str) -> bool:
        return '<' in lines and '>' in lines

    @staticmethod
    def __get_tag(lines: str) -> str:
        tag = lines[lines.find('<') + 1:lines.find('>')]
        return tag

    @staticmethod
    def __get_list(lines: str, tag: str) -> list[str]:
        # You gonna ask me wtf is here?
        # Dont ask me anything i am pythonist, and that line is working great
        # Is it fast enough? Oh, its lightning fast
        return [i[:-(len(tag) + 3)] for i in lines.split(f'<{tag}>')[1:]]

    @staticmethod
    def __split_list(lines, tag):
        return [i[:-(len(tag) + 3)] for i in lines.split(f'<{tag}>')[1:]]

    @staticmethod
    def __match_tag(lines: str) -> tuple[str, str, str]:
        tag = lines[lines.find('<') + 1:lines.find('>')]
        info = lines[len(tag) + 2:lines.find(f'</{tag}>')]
        rest = lines[lines.find(f'</{tag}>') + len(tag) + 3:]
        return tag, info, rest


if __name__ == '__main__':
    with open("Myxml.xml", "r", encoding="utf16") as input_file:
        parser = XMLParser(input_file.read(), 'pair', 'schedule')
        with open('Mycsv.csv', 'w', encoding='utf-16') as out_file:
            out_file.write(parser.to_csv())


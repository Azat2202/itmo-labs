import re
import time


class XmlParser:
    out = ''
    split_list_re = re.compile(r'<(?P<tag>\b\w+\b)[\w ]*>\s?(.*?)[ \s]*</\1>', flags=re.S)
    has_tags_re = re.compile(r'<(?P<tag>\b\w+\b)[\w ]*>\s?(?P<info>.*?)</\1>', flags=re.S)
    get_tag_re = re.compile(r'<(?P<tag>\b\w+\b)[\w ]*>', flags=re.S)
    match_tag_re = re.compile(r'<(?P<tag>\b\w+\b)[\w ]*>\s?(?P<info>.*?)</\1>\s?(?P<rest>.*)', flags=re.S)
    del_start_re = re.compile(r'^ {0,4}\t?', flags=re.M)

    def __init__(self, xml_str):
        self.xml_doc = xml_str
        self.__del_info_data()

    def to_jaml(self):
        self.out = ''
        self.__find_sub(self.xml_doc)
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
                for i in self.__split_list(line):
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
                for element in self.__split_list(line):
                    self.__find_sub(element, tab_count + 2, needs_minus=True)
                return
            self.out += tag + ':\n'
            self.__find_sub(info, tab_count + 1)
            self.__find_sub(rest, tab_count)

    def __split_list(self, line):
        info = self.split_list_re.findall(line)
        return [i[1] for i in info]

    def __has_tags(self, lines):
        matched = self.has_tags_re.search(lines)
        return bool(matched)

    def __get_tag(self, lines):
        match = self.get_tag_re.search(lines)
        return match.group("tag")

    def __match_tag(self, lines):
        matched = self.match_tag_re.search(lines)
        del_start_spaces = self.del_start_re.sub('', matched.group("info"))
        return matched.group("tag"), del_start_spaces, matched.group("rest")

    def __del_info_data(self):
        self.xml_doc = re.sub(r'<\?.*\?>\\n', '', self.xml_doc)


if __name__ == '__main__':
    start_time = time.time()
    for i in range(100):
        with open("Myxml.xml", "r", encoding="utf16") as input_file:
            parser = XmlParser(input_file.read())
            with open('Myyaml.yaml', 'w', encoding="utf-16") as out_file:
                out_file.write(parser.to_jaml())
    print("Время стократного выполнения программы с регулярными выражениями: ", time.time() - start_time)
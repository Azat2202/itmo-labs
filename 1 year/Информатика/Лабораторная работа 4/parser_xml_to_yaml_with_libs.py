import xmlplain
import time


if __name__ == '__main__':
    start_time = time.time()
    for i in range(100):
        with open("Myxml.xml", encoding='utf-16') as inf:
            root = xmlplain.xml_to_obj(inf, strip_space=True, fold_dict=True)
        with open("Myyaml.yaml", "w", encoding='utf-16') as outf:
            xmlplain.obj_to_yaml(root, outf)
    print('Время стократного выполнения программы с библиотекой: ', time.time() - start_time)

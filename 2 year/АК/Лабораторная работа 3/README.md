# Лабораторная работа №3 по Архитектуре Компьютера

- Сиразетдинов Азат Ниязович, P3216
- ```asm | stack | neum | hw | tick -> instr | struct | trap -> stream | mem | cstr | prob1 | spi```
- Упрощенный вариант

## Язык программирования

### Синтаксис

``` ebnf
program ::= { line }

line ::= label [ comment ] "\n"
       | instr [ comment ] "\n"
       | [ comment ] "\n"

label ::= label_name ":"

instr ::= op0 
        | op1 variable
        | "WORD" word_data

op0 ::= "DUP" 
      | "OVER"
      | "POP"
      | "SWAP"
      | "LOAD"
      | "SAVE"
      | "INC"
      | "DEC"
      | "ADD"
      | "SUB"
      | "DIV"
      | "MUL"
      | "XOR"
      | "TEST"
      | "JUMP"
      | "JZ"
      | "HALT"

op1 ::= "PUSH"

label_name ::= <any of "a-z A-Z _"> { <any of "a-z A-Z 0-9 _"> }

comment ::= ";" <any symbols except "\n">

word_data ::= number | string

variable ::= number | label_name

string ::= "\"" <any symbols except "\n"> "\""

number ::= 0x <any of "0-9 ABCDEF">
```

Поддерживаются однострочные комментарии, начинающиеся с ;

- ```DUP``` - продублировать вершину стека данных ```[a] -> [a, a]```
- ```OVER``` - дублировать второй элемент на стеке данных через первый ```[a, b] -> [a, b, a]```
- ```POP``` - удалить значение с вершины стека данных ```[a] -> []```
- ```SWAP``` - поменять местами два значения на вершине стека данных ```[a, b] -> [b, a]```
- ```LOAD``` - загрузить из памяти значение по адресу с вершины стека ```[address] -> [value_from_memory]```
- ```SAVE``` - взять адрес с вершины стека данных и записать в память значение, следующее после вершины стека
  данных ```[value, address] -> []```
- ```INC``` - увеличить значения на вершине стека данных на 1 ```[a] -> [a + 1]```
- ```DEC``` - уменьшить значения на вершине стека данных на 1 ```[a] -> [a - 1]```
- ```ADD``` - сумма двух значений на вершине стека данных ```[a, b] -> [b + a]```
- ```SUB``` - разность двух значений на вершине стека данных (первое вычитается из второго) ```[a, b] -> [a - b]```
- ```DIV``` - целочисленное деление двух значений на вершине стека данных (второе делится на
  первое) ```[a, b] -> [a // b]```
- ```XOR``` - побитовая операция исключающего ИЛИ ```[a, b] -> [a ^ b]```
- ```TEST``` - выставить z_flag по вершине стека ```[a] -> [a]```
- ```MUL``` - произведение двух значений на вершине стека данных ```[a, b] -> [b * a]```
- ```JUMP``` - безусловный переход на адрес с вершины стека ```[a] -> []```
- ```JZ``` - переход на адрес с вершины стека, если второй элемент равен 0 ```[a, b] -> [a] ```
- ```PUSH number``` - положить число на стек ```[] -> [2]```
- ```PUSH label``` - положить адрес метки на стек ```[] -> [0x801]```
- ```HALT``` - остановка программы

### Семантика

- Язык предполагает последовательное исполнение
- Глобальная область видимости меток
- В программе не может быть дублирующихся меток, определенных в разных местах с одним именем.
- В программе должна быть определена метка ```_start``` указывающая на адрес первой исполняемой команды
- Метки для переходов определяются на отдельных строчках:
  ``` asm
  label: 
      INC
  ```
  И в другом месте (неважно, до или после определения) сослаться на эту метку:
  ``` asm 
  jmp label   ; --> `jmp 123`, где 123 - номер инструкции после объявления метки
  ```

## Организация памяти

Модель памяти процессора:

- Машинное слово – не определено. Реализуется высокоуровневой структурой данных. Операнд – 32-битный. Интерпретируется
  как беззнаковое целое число. При попытке чтения данных из ячейки с данными исполнение программы аварийно завершится
- Адресация - абсолютная. Только прямая загрузка операнда в стек
- Программа и данные хранятся в общей памяти согласно архитектуре Фон-Неймановского процессора. Программа состоит из
  набора инструкций, последняя инструкция – HALT
- Операция записи в память перезапишет ячейку памяти как ячейку с данными. Программист имеет доступ на чтение/запись в
  любую ячейку памяти.

```
           memory
+----------------------------+
| 00 :       ...             |
| 01 :     variables         |
| 02 :       ...             |
| 03 :                       |
|           ...              |
| n  : program start         |
|           ...              |
| k :        ...             |
| k+1 :    variables         |
| k+2 :      ...             |
+----------------------------+
```

- Адрес начала программы задается меткой ```_start:```
- Программисту доступен стек и память 
- Константы отсутствуют, память динамическая

## Система команд

Особенности процессора:

- Машинное слово - не определено
- Доступ к памяти осуществляется по адресу из специального регистра AR. Значение в нем может быть защелкнуто либо из PC,
  либо из вершины стека
- Устройства ввода вывода отражены в памяти. Адрес ВУ - 0x65
- Обработка данных осуществляется в стеке. Данные попадают в стек из памяти
- Поток управления:
    - Значение PC инкриминируется после исполнения каждой инструкции,
    - Условные (JZ) и безусловные (JUMP) переходы.

Набор инструкций:

| Инструкция | Кол-во тактов | Описание                                                                                                                             |
|------------|---------------|--------------------------------------------------------------------------------------------------------------------------------------|
| DUP        | 1             | продублировать вершину стека данных ```[a] -> [a, a]```                                                                              |
| POP        | 1             | удалить значение с вершины стека данных ```[a] -> []```                                                                              |
| OVER       | 3             | дублировать второй элемент на стеке данных через первый ```[a, b] -> [a, b, a]```                                                    |
| SWAP       | 3             | поменять местами два значения на вершине стека данных ```[a, b] -> [b, a]```                                                         |
| LOAD       | 2             | загрузить из памяти значение по адресу с вершины стека ```[address] -> [value_from_memory]```                                        |
| SAVE       | 4             | взять адрес с вершины стека данных и записать в память значение, следующее после вершины стека   данных ```[value, address] -> []``` |
| INC        | 1             | увеличить значения на вершине стека данных на 1 ```[a] -> [a + 1]```                                                                 |
| DEC        | 1             | уменьшить значения на вершине стека данных на 1 ```[a] -> [a - 1]```                                                                 |
| ADD        | 2             | сумма двух значений на вершине стека данных ```[a, b] -> [b + a]```                                                                  |
| SUB        | 2             | разность двух значений на вершине стека данных (первое вычитается из второго) ```[a, b] -> [a - b]```                                |
| DIV        | 2             | целочисленное деление двух значений на вершине стека данных (второе делится на первое) ```[a, b] -> [a // b]```                      |
| XOR        | 1             | побитовая операция исключающего ИЛИ ```[a, b] -> [a ^ b]```                                                                          |
| TEST       | 1             | выставить z_flag по вершине стека ```[a] -> [a]```                                                                                   |
| MUL        | 2             | произведение двух значений на вершине стека данных ```[a, b] -> [b * a]```                                                           |
| JUMP       | 2             | безусловный переход на адрес с вершины стека ```[a] -> []```                                                                         |
| JZ         | 1/2           | переход на адрес с вершины стека, если второй элемент равен 0 ```[a, b] -> [a] ```                                                   |
| PUSH       | 1             | положить число на стек ```[] -> [2]```                                                                                               |
| HALT       | 1             | остановка программы                                                                                                                  |

Единственный способ прямого взаимодействия со стеком - операция ```PUSH```. Все остальные операции берут значения в
верхушки стека

Способ кодирования инструкций:

Машинный код преобразуется в список JSON, где один элемент списка — это одна инструкция. Индекс инструкции в списке –
адрес этой инструкции в памяти. Пример машинного слова:

```
{
  "opcode": "push",
  "arg": 101
},
```

## Транслятор

Интерфейс командной строки: ```python translator.py <source_file> <target_file>```

Реализовано в модуле [translator](https://github.com/Azat2202/AK_lab3/blob/main/ak_lab3/translator.py)

Этапы трансляции:

1) Препроцессинг: Удаление комментариев, превращение каждой строки в список команд и данных разделенных пробелом
2) Трансляция кода в инструкции, метки остаются неразмеченными
3) Подстановка меток в инструкции

Правила трансляции:

- Одна инструкция - одна строка
- Метки отделены строками
- Ссылаться можно только на существующие метки

## Модель процессора

Интерфейс командной строки: ```machine.py [-h] [--iotype [IOTYPE]] input_file output_file```

Реализован в модуле [machine](https://github.com/Azat2202/AK_lab3/blob/main/ak_lab3/machine.py)

### DataPath

[//]: # (https://asciiflow.com/#/share/eJzFVstKw0AU%2FZUwK7UtfWwK3YkrwVJQAy6CZWhHW0yTkk6xtVSk%2BAkh%2BhGuupR%2BTb7ESTNJk3nkMVl4Ccm9meQ87syEbIAFZwj0rKVp1oEJ18gBPbAxwMoAvXar1akbYE3STrdLMoxWmBQG0Gj47qHIoSmEYVgJlsQAgz1fLiaVcMcQQ56Fi7I%2BOZ4FhqMXgX57Hgwik%2BeTFNm9kVanW300s521GirV7f2KZpbV7H990sccBMc8R6meBnmf91S5T0Gup9SWm%2BqUz1dn6iAh8%2F3gLsxzmbQHkSkFn5SNB2L4mUGFVX7WOleZT%2FY%2Bp4WcrweZjo%2F3E4NRHShKhQg7p5u%2Bd6AncS3HVumh4D1ho7gdwPLHavfsc4QJ23Of7t9dXw9W2i7GCOvT6ligkW2NMzl4LSc3ru9%2B%2BO7PEYsk39Xc5DVSttcFBLluZLheqr0FcAstBSGb1Gzp%2FtHL5W30djS9JsSjyRA6MadBIpk1w6JZzqO3L%2BqRIYzo3oZPJnyW8jF5rZ3AfW%2FUmheP4QKPwC9v9BC38L7kCLMmI2WLiGm0U8a4BmaRaHQHiqCTAHkGinAVMyQAY0LKEX6B4q9QNvL%2FHqW%2BLtJIN0jSrkJ4Ai3RD5dQY0UuQV0d8cq2sGObGvfDUVWpbk2xULkinrRWwCux5HKYwBZs%2FwD5cyhO&#41;)

```
                                                    +----------+                 
                                                    |          |                 
           +-----------+                            |  Memory  |                 
  push ----+           |                            |          |<-- read         
           |   data    |                      dt_out|          |                 
  pop------+   stack   |          sel        +------+          |<-- wrire        
           |           |           |         | dt_in|          |                 
wr_second--+           |         +-v-+       |  +-->+----------+                 
           +-----------+         | M |       |  |   |   IO     |                 
wr_first---+           |         | U +-------+  |   |          |                 
           |    TOS  | |<--------+ X |       |  |   +----------+                 
           |         | |         |   |<---+  |  |        ^ address               
       (0) ++--------+-+         +---+    |  |  |        |                       
         |  |        |  |(0)              |  |  |      +-+----+                  
         v  v        v  v                 |  |  |      |  AR  | <--latch_ar      
        +----+      +----+                |  |  |      +------+                  
   top->|MUX |      |MUX |<--second       |  |  |        ^                       
        +--+-+      +-+--+                |  |  |        |                       
           |          |                   |  |  |    +---+----+                  
           v          v                   |  |  |    |   MUX  |                  
         -------    ------                |  |  |    +--------+                  
         \      \  /      /               |  |  |      ^     ^    +----+         
          \      \/      /z_flag          |  |  |      |     |    |    |         
   ~-+/*^->\    ALU     /--+              |  |  |      |    ++----++   | latch_pc
    +1 -1   \          /   |              |  |  |      |    |  PC  | <-+------   
             --+-------    |              |  |  |      |    +------+   |         
               |           |              |  |  |      |       ^       +1        
               |           |              |  |  |      |       |       |         
               +-----------+--------------+--+--+------+    +--+---+   |         
                           |                 |         |    | MUX  |   |         
                           |                 |         |    +------+   |         
                           |                 |         |     ^  ^ ^    |         
                           v                 |         |     |  | +- --+         
                      +-------------+ instr  |         +-----+  |                
                      |             +--------+                  |                
                      |   Control   |                           |                
                      |    Unit     +---------------------------+                
                      |             |address                                     
                      +-------------+                                            
```

Реализован в классе DataPath

memory - однопортовая память, поэтому либо читаем, либо пишем.

Сигналы (обрабатываются за один такт, реализованы в виде методов класса):

- ```latch_pc``` - защелкнуть выбранное значение в program counter
- ```latch_ar``` - защелкнуть выбранное значение в address register
- ```write``` - записать выбранное значение в память по адресу ar
- ```read``` - прочитать значение из памяти по адресу ar
- ```push``` - положить выбранное значение на стек
- ```pop``` - удалить значение со стека (значение никуда не передается)
- ```wr_second``` - записать значение на вторую позицию стека
- ```wr_first``` - записать значение на вершину стека
  Флаги:
- ```zero``` - отражает нулевой результат операции в АЛУ

### Control Unit

[//]: # (https://asciiflow.com/#/share/eJytkk0KwjAQha9SZt2VC63d1o07QdwFJNSohZpIO4K1dOcRpN5FPI0nMdoq9DepNmSRGfK9N5NJDJzuGNjgCI6B8I0F9xBM8GnEApmOCRwJ2GNrZBKI5GlgDeUJ2RFlQMBoWY%2FLvesmhCsUq5k6RmGU3nOlObK9UqkSOOLAkQUdKa2qtVvQfb2qeTFbYLoaaim21dNGTXmIwcFFT3AF1VzehLlilU3qfet61u3pq6zVx2f1Rf06hQbFgrX8%2FTWEkqr1ySnvNap3EHobTv0wJ07LtU83%2FflUqLQr1f%2FbZocJRVpupZ4q7%2FSWATOKWw2Fpit6zN%2B9QwLJE2RWRN4%3D&#41;)

```
                            +---------------+
                            |               |
                 +--------->|    Step       |
                 |          |    Counter    |
                 |          |               |
                 |          +--------+------+
                 |                   |       
          +------+-------+           |       
          |              |           |       
          |  Instruction |           |       
   +------+  Decoder     |<----------+       
   |      |              |                   
   |      |              +------------+      
   |      +---+----------+            |      
   |          |      ^                |      
   |          |      |                |      
instr     signals    |z_flag        address  
   |          |      |                |      
   |          v      |                |      
   |      +----------+---+            |      
   |      |   Data       |            |      
   +----->|   Path       |<-----------+      
          |              |                   
          +--------------+                   
```

Реализовано в классе ```ControlUnit```

- Hardwired (реализовано полностью на Python).
- Выполняет предварительную инициализацию машины -- выполняет список инструкций, чтобы защелкнуть адрес первой
  инструкции в pc(метод initialize_datapath)
- Выполнение и декодирование инструкций происходит в методе decode_and_execute_instruction.
- tick нужен для подсчета тактов

Особенности работы модели:

- Цикл симуляции осуществляется в функции simulation.
- Шаг моделирования соответствует одной инструкции с выводом состояния в журнал.
- Для журнала состояний процессора используется стандартный модуль logging.
- Количество инструкций для моделирования лимитировано (1000).
- Остановка моделирования осуществляется при:
    - превышении лимита количества выполняемых инструкций;
    - исключении StopIteration -- если выполнена инструкция halt.

## Тестирование

- Тестирование выполняется при помощи golden test-ов.
- Настройка golden тестирования находится в [файле](https://github.com/Azat2202/AK_lab3/blob/main/tests/test_golden.py)
- Конфигурация golden test-ов лежит в [директории](https://github.com/Azat2202/AK_lab3/blob/main/tests/golden/)

Запустить тесты: ```poetry run pytest .```
Обновить конфигурацию golden tests: ```poetry run pytest . --update-goldens```

CI при помощи Github Actions:

CI-linter:

```yaml
name: CI - linter

on: [ push ]

jobs:
  build:
    runs-on: ubuntu-latest
    strategy:
      matrix:
        python-version: [ "3.12" ]
    steps:
      - name: Set up Python ${{ matrix.python-version }}
        uses: actions/setup-python@v3
        with:
          python-version: ${{ matrix.python-version }}
      - name: Install poetry
        run: |
          python -m pip install --upgrade pip
          curl -sSL https://install.python-poetry.org | python3 -
          export PATH="$HOME/.local/bin:$PATH"
      - uses: actions/checkout@v4
      - name: Install dependencies
        run: |
          poetry install
          poetry self add 'poethepoet[poetry_plugin]'
      - name: Run linters
        run: |
          poetry poe lint
```

CI-tester:

```yaml

name: CI - tester

on: [ push ]

jobs:
  build:
    runs-on: ubuntu-latest
    strategy:
      matrix:
        python-version: [ "3.12" ]
    steps:
      - name: Set up Python ${{ matrix.python-version }}
        uses: actions/setup-python@v3
        with:
          python-version: ${{ matrix.python-version }}
      - name: Install poetry
        run: |
          python -m pip install --upgrade pip
          curl -sSL https://install.python-poetry.org | python3 -
          export PATH="$HOME/.local/bin:$PATH"
      - uses: actions/checkout@v4
      - name: Install dependencies
        run: |
          poetry install
          poetry self add 'poethepoet[poetry_plugin]'
      - name: Test
        run: |
          poetry poe test
```

где:

- ```poetry``` - управления зависимостями для языка программирования Python.
- ```pytest``` - утилита для запуска тестов.
- ```mypy``` - утилита для статического анализа типов в коде
- ```pylint``` - утилита для статического анализа кода
- ```black``` - утилита для форматирования кода

Пример использования и журнал работы процессора на примере add:

```
>> python -m ak_lab3.translator .\examples\asm\hello_world.asm .\examples\json\hello_world.json
>> python -m ak_lab3.machine .\examples\json\hello_world.json .\examples\input\empty.txt 
DEBUG:root:TICK:    0 PC:   14 AR:    0 Z_FLAG: 0       {'opcode': 'push', 'arg': 0}            DATA_STACK: [] 
DEBUG:root:TICK:    4 PC:   15 AR:   14 Z_FLAG: 0       {'opcode': 'dup'}                       DATA_STACK: [0]
DEBUG:root:TICK:    8 PC:   16 AR:   15 Z_FLAG: 1       {'opcode': 'load'}                      DATA_STACK: [0, 0]
DEBUG:root:TICK:   13 PC:   17 AR:    0 Z_FLAG: 1       {'opcode': 'test'}                      DATA_STACK: [0, 72]
DEBUG:root:TICK:   17 PC:   18 AR:   17 Z_FLAG: 0       {'opcode': 'push', 'arg': 25}           DATA_STACK: [0, 72]
DEBUG:root:TICK:   21 PC:   19 AR:   18 Z_FLAG: 0       {'opcode': 'jz'}                        DATA_STACK: [0, 72, 25]
DEBUG:root:TICK:   25 PC:   20 AR:   19 Z_FLAG: 0       {'opcode': 'push', 'arg': 101}          DATA_STACK: [0, 72]
DEBUG:root:TICK:   29 PC:   21 AR:   20 Z_FLAG: 0       {'opcode': 'save'}                      DATA_STACK: [0, 72, 101]
DEBUG:root:TICK:   36 PC:   22 AR:  101 Z_FLAG: 0       {'opcode': 'inc'}                       DATA_STACK: [0]
DEBUG:root:TICK:   40 PC:   23 AR:   22 Z_FLAG: 0       {'opcode': 'push', 'arg': 15}           DATA_STACK: [1]
DEBUG:root:TICK:   44 PC:   24 AR:   23 Z_FLAG: 0       {'opcode': 'jump'}                      DATA_STACK: [1, 15]
DEBUG:root:TICK:   49 PC:   15 AR:   24 Z_FLAG: 0       {'opcode': 'dup'}                       DATA_STACK: [1]
DEBUG:root:TICK:   53 PC:   16 AR:   15 Z_FLAG: 0       {'opcode': 'load'}                      DATA_STACK: [1, 1]
DEBUG:root:TICK:   58 PC:   17 AR:    1 Z_FLAG: 0       {'opcode': 'test'}                      DATA_STACK: [1, 101]
DEBUG:root:TICK:   62 PC:   18 AR:   17 Z_FLAG: 0       {'opcode': 'push', 'arg': 25}           DATA_STACK: [1, 101]
DEBUG:root:TICK:   66 PC:   19 AR:   18 Z_FLAG: 0       {'opcode': 'jz'}                        DATA_STACK: [1, 101, 25]
DEBUG:root:TICK:   70 PC:   20 AR:   19 Z_FLAG: 0       {'opcode': 'push', 'arg': 101}          DATA_STACK: [1, 101]
DEBUG:root:TICK:   74 PC:   21 AR:   20 Z_FLAG: 0       {'opcode': 'save'}                      DATA_STACK: [1, 101, 101]
DEBUG:root:TICK:   81 PC:   22 AR:  101 Z_FLAG: 0       {'opcode': 'inc'}                       DATA_STACK: [1]
DEBUG:root:TICK:   85 PC:   23 AR:   22 Z_FLAG: 0       {'opcode': 'push', 'arg': 15}           DATA_STACK: [2]
DEBUG:root:TICK:   89 PC:   24 AR:   23 Z_FLAG: 0       {'opcode': 'jump'}                      DATA_STACK: [2, 15]
DEBUG:root:TICK:   94 PC:   15 AR:   24 Z_FLAG: 0       {'opcode': 'dup'}                       DATA_STACK: [2]
DEBUG:root:TICK:   98 PC:   16 AR:   15 Z_FLAG: 0       {'opcode': 'load'}                      DATA_STACK: [2, 2]
DEBUG:root:TICK:  103 PC:   17 AR:    2 Z_FLAG: 0       {'opcode': 'test'}                      DATA_STACK: [2, 108]
DEBUG:root:TICK:  107 PC:   18 AR:   17 Z_FLAG: 0       {'opcode': 'push', 'arg': 25}           DATA_STACK: [2, 108]
DEBUG:root:TICK:  111 PC:   19 AR:   18 Z_FLAG: 0       {'opcode': 'jz'}                        DATA_STACK: [2, 108, 25]
DEBUG:root:TICK:  115 PC:   20 AR:   19 Z_FLAG: 0       {'opcode': 'push', 'arg': 101}          DATA_STACK: [2, 108]
DEBUG:root:TICK:  119 PC:   21 AR:   20 Z_FLAG: 0       {'opcode': 'save'}                      DATA_STACK: [2, 108, 101]
DEBUG:root:TICK:  126 PC:   22 AR:  101 Z_FLAG: 0       {'opcode': 'inc'}                       DATA_STACK: [2]
DEBUG:root:TICK:  130 PC:   23 AR:   22 Z_FLAG: 0       {'opcode': 'push', 'arg': 15}           DATA_STACK: [3]
DEBUG:root:TICK:  134 PC:   24 AR:   23 Z_FLAG: 0       {'opcode': 'jump'}                      DATA_STACK: [3, 15]
DEBUG:root:TICK:  139 PC:   15 AR:   24 Z_FLAG: 0       {'opcode': 'dup'}                       DATA_STACK: [3]
DEBUG:root:TICK:  143 PC:   16 AR:   15 Z_FLAG: 0       {'opcode': 'load'}                      DATA_STACK: [3, 3]
DEBUG:root:TICK:  148 PC:   17 AR:    3 Z_FLAG: 0       {'opcode': 'test'}                      DATA_STACK: [3, 108]
DEBUG:root:TICK:  152 PC:   18 AR:   17 Z_FLAG: 0       {'opcode': 'push', 'arg': 25}           DATA_STACK: [3, 108]
DEBUG:root:TICK:  156 PC:   19 AR:   18 Z_FLAG: 0       {'opcode': 'jz'}                        DATA_STACK: [3, 108, 25]
DEBUG:root:TICK:  160 PC:   20 AR:   19 Z_FLAG: 0       {'opcode': 'push', 'arg': 101}          DATA_STACK: [3, 108]
DEBUG:root:TICK:  164 PC:   21 AR:   20 Z_FLAG: 0       {'opcode': 'save'}                      DATA_STACK: [3, 108, 101]
DEBUG:root:TICK:  171 PC:   22 AR:  101 Z_FLAG: 0       {'opcode': 'inc'}                       DATA_STACK: [3]
DEBUG:root:TICK:  175 PC:   23 AR:   22 Z_FLAG: 0       {'opcode': 'push', 'arg': 15}           DATA_STACK: [4]
DEBUG:root:TICK:  179 PC:   24 AR:   23 Z_FLAG: 0       {'opcode': 'jump'}                      DATA_STACK: [4, 15]
DEBUG:root:TICK:  184 PC:   15 AR:   24 Z_FLAG: 0       {'opcode': 'dup'}                       DATA_STACK: [4]
DEBUG:root:TICK:  188 PC:   16 AR:   15 Z_FLAG: 0       {'opcode': 'load'}                      DATA_STACK: [4, 4]
DEBUG:root:TICK:  193 PC:   17 AR:    4 Z_FLAG: 0       {'opcode': 'test'}                      DATA_STACK: [4, 111]
DEBUG:root:TICK:  197 PC:   18 AR:   17 Z_FLAG: 0       {'opcode': 'push', 'arg': 25}           DATA_STACK: [4, 111]
DEBUG:root:TICK:  201 PC:   19 AR:   18 Z_FLAG: 0       {'opcode': 'jz'}                        DATA_STACK: [4, 111, 25]
DEBUG:root:TICK:  205 PC:   20 AR:   19 Z_FLAG: 0       {'opcode': 'push', 'arg': 101}          DATA_STACK: [4, 111]
DEBUG:root:TICK:  209 PC:   21 AR:   20 Z_FLAG: 0       {'opcode': 'save'}                      DATA_STACK: [4, 111, 101]
DEBUG:root:TICK:  216 PC:   22 AR:  101 Z_FLAG: 0       {'opcode': 'inc'}                       DATA_STACK: [4]
DEBUG:root:TICK:  220 PC:   23 AR:   22 Z_FLAG: 0       {'opcode': 'push', 'arg': 15}           DATA_STACK: [5]
DEBUG:root:TICK:  224 PC:   24 AR:   23 Z_FLAG: 0       {'opcode': 'jump'}                      DATA_STACK: [5, 15]
DEBUG:root:TICK:  229 PC:   15 AR:   24 Z_FLAG: 0       {'opcode': 'dup'}                       DATA_STACK: [5] 
DEBUG:root:TICK:  233 PC:   16 AR:   15 Z_FLAG: 0       {'opcode': 'load'}                      DATA_STACK: [5, 5]
DEBUG:root:TICK:  238 PC:   17 AR:    5 Z_FLAG: 0       {'opcode': 'test'}                      DATA_STACK: [5, 44]
DEBUG:root:TICK:  242 PC:   18 AR:   17 Z_FLAG: 0       {'opcode': 'push', 'arg': 25}           DATA_STACK: [5, 44]
DEBUG:root:TICK:  246 PC:   19 AR:   18 Z_FLAG: 0       {'opcode': 'jz'}                        DATA_STACK: [5, 44, 25]
DEBUG:root:TICK:  250 PC:   20 AR:   19 Z_FLAG: 0       {'opcode': 'push', 'arg': 101}          DATA_STACK: [5, 44]
DEBUG:root:TICK:  254 PC:   21 AR:   20 Z_FLAG: 0       {'opcode': 'save'}                      DATA_STACK: [5, 44, 101]
DEBUG:root:TICK:  261 PC:   22 AR:  101 Z_FLAG: 0       {'opcode': 'inc'}                       DATA_STACK: [5]
DEBUG:root:TICK:  265 PC:   23 AR:   22 Z_FLAG: 0       {'opcode': 'push', 'arg': 15}           DATA_STACK: [6]
DEBUG:root:TICK:  269 PC:   24 AR:   23 Z_FLAG: 0       {'opcode': 'jump'}                      DATA_STACK: [6, 15]
DEBUG:root:TICK:  274 PC:   15 AR:   24 Z_FLAG: 0       {'opcode': 'dup'}                       DATA_STACK: [6]
DEBUG:root:TICK:  278 PC:   16 AR:   15 Z_FLAG: 0       {'opcode': 'load'}                      DATA_STACK: [6, 6]
DEBUG:root:TICK:  283 PC:   17 AR:    6 Z_FLAG: 0       {'opcode': 'test'}                      DATA_STACK: [6, 32]
DEBUG:root:TICK:  287 PC:   18 AR:   17 Z_FLAG: 0       {'opcode': 'push', 'arg': 25}           DATA_STACK: [6, 32]
DEBUG:root:TICK:  291 PC:   19 AR:   18 Z_FLAG: 0       {'opcode': 'jz'}                        DATA_STACK: [6, 32, 25]
DEBUG:root:TICK:  295 PC:   20 AR:   19 Z_FLAG: 0       {'opcode': 'push', 'arg': 101}          DATA_STACK: [6, 32]
DEBUG:root:TICK:  299 PC:   21 AR:   20 Z_FLAG: 0       {'opcode': 'save'}                      DATA_STACK: [6, 32, 101]
DEBUG:root:TICK:  306 PC:   22 AR:  101 Z_FLAG: 0       {'opcode': 'inc'}                       DATA_STACK: [6]
DEBUG:root:TICK:  310 PC:   23 AR:   22 Z_FLAG: 0       {'opcode': 'push', 'arg': 15}           DATA_STACK: [7]
DEBUG:root:TICK:  314 PC:   24 AR:   23 Z_FLAG: 0       {'opcode': 'jump'}                      DATA_STACK: [7, 15]
DEBUG:root:TICK:  319 PC:   15 AR:   24 Z_FLAG: 0       {'opcode': 'dup'}                       DATA_STACK: [7]
DEBUG:root:TICK:  323 PC:   16 AR:   15 Z_FLAG: 0       {'opcode': 'load'}                      DATA_STACK: [7, 7]
DEBUG:root:TICK:  328 PC:   17 AR:    7 Z_FLAG: 0       {'opcode': 'test'}                      DATA_STACK: [7, 119]
DEBUG:root:TICK:  332 PC:   18 AR:   17 Z_FLAG: 0       {'opcode': 'push', 'arg': 25}           DATA_STACK: [7, 119]
DEBUG:root:TICK:  336 PC:   19 AR:   18 Z_FLAG: 0       {'opcode': 'jz'}                        DATA_STACK: [7, 119, 25]
DEBUG:root:TICK:  340 PC:   20 AR:   19 Z_FLAG: 0       {'opcode': 'push', 'arg': 101}          DATA_STACK: [7, 119]
DEBUG:root:TICK:  344 PC:   21 AR:   20 Z_FLAG: 0       {'opcode': 'save'}                      DATA_STACK: [7, 119, 101]
DEBUG:root:TICK:  351 PC:   22 AR:  101 Z_FLAG: 0       {'opcode': 'inc'}                       DATA_STACK: [7]
DEBUG:root:TICK:  355 PC:   23 AR:   22 Z_FLAG: 0       {'opcode': 'push', 'arg': 15}           DATA_STACK: [8]
DEBUG:root:TICK:  359 PC:   24 AR:   23 Z_FLAG: 0       {'opcode': 'jump'}                      DATA_STACK: [8, 15]
DEBUG:root:TICK:  364 PC:   15 AR:   24 Z_FLAG: 0       {'opcode': 'dup'}                       DATA_STACK: [8]
DEBUG:root:TICK:  368 PC:   16 AR:   15 Z_FLAG: 0       {'opcode': 'load'}                      DATA_STACK: [8, 8]
DEBUG:root:TICK:  373 PC:   17 AR:    8 Z_FLAG: 0       {'opcode': 'test'}                      DATA_STACK: [8, 111]
DEBUG:root:TICK:  377 PC:   18 AR:   17 Z_FLAG: 0       {'opcode': 'push', 'arg': 25}           DATA_STACK: [8, 111]
DEBUG:root:TICK:  381 PC:   19 AR:   18 Z_FLAG: 0       {'opcode': 'jz'}                        DATA_STACK: [8, 111, 25]
DEBUG:root:TICK:  385 PC:   20 AR:   19 Z_FLAG: 0       {'opcode': 'push', 'arg': 101}          DATA_STACK: [8, 111]
DEBUG:root:TICK:  389 PC:   21 AR:   20 Z_FLAG: 0       {'opcode': 'save'}                      DATA_STACK: [8, 111, 101]
DEBUG:root:TICK:  396 PC:   22 AR:  101 Z_FLAG: 0       {'opcode': 'inc'}                       DATA_STACK: [8]
DEBUG:root:TICK:  400 PC:   23 AR:   22 Z_FLAG: 0       {'opcode': 'push', 'arg': 15}           DATA_STACK: [9]
DEBUG:root:TICK:  404 PC:   24 AR:   23 Z_FLAG: 0       {'opcode': 'jump'}                      DATA_STACK: [9, 15]
DEBUG:root:TICK:  409 PC:   15 AR:   24 Z_FLAG: 0       {'opcode': 'dup'}                       DATA_STACK: [9]
DEBUG:root:TICK:  413 PC:   16 AR:   15 Z_FLAG: 0       {'opcode': 'load'}                      DATA_STACK: [9, 9]
DEBUG:root:TICK:  418 PC:   17 AR:    9 Z_FLAG: 0       {'opcode': 'test'}                      DATA_STACK: [9, 114]
DEBUG:root:TICK:  422 PC:   18 AR:   17 Z_FLAG: 0       {'opcode': 'push', 'arg': 25}           DATA_STACK: [9, 114]
DEBUG:root:TICK:  426 PC:   19 AR:   18 Z_FLAG: 0       {'opcode': 'jz'}                        DATA_STACK: [9, 114, 25]
DEBUG:root:TICK:  430 PC:   20 AR:   19 Z_FLAG: 0       {'opcode': 'push', 'arg': 101}          DATA_STACK: [9, 114]
DEBUG:root:TICK:  434 PC:   21 AR:   20 Z_FLAG: 0       {'opcode': 'save'}                      DATA_STACK: [9, 114, 101]
DEBUG:root:TICK:  441 PC:   22 AR:  101 Z_FLAG: 0       {'opcode': 'inc'}                       DATA_STACK: [9]
DEBUG:root:TICK:  445 PC:   23 AR:   22 Z_FLAG: 0       {'opcode': 'push', 'arg': 15}           DATA_STACK: [10]
DEBUG:root:TICK:  449 PC:   24 AR:   23 Z_FLAG: 0       {'opcode': 'jump'}                      DATA_STACK: [10, 15]
DEBUG:root:TICK:  454 PC:   15 AR:   24 Z_FLAG: 0       {'opcode': 'dup'}                       DATA_STACK: [10]
DEBUG:root:TICK:  458 PC:   16 AR:   15 Z_FLAG: 0       {'opcode': 'load'}                      DATA_STACK: [10, 10]
DEBUG:root:TICK:  463 PC:   17 AR:   10 Z_FLAG: 0       {'opcode': 'test'}                      DATA_STACK: [10, 108]
DEBUG:root:TICK:  467 PC:   18 AR:   17 Z_FLAG: 0       {'opcode': 'push', 'arg': 25}           DATA_STACK: [10, 108]
DEBUG:root:TICK:  471 PC:   19 AR:   18 Z_FLAG: 0       {'opcode': 'jz'}                        DATA_STACK: [10, 108, 25]
DEBUG:root:TICK:  475 PC:   20 AR:   19 Z_FLAG: 0       {'opcode': 'push', 'arg': 101}          DATA_STACK: [10, 108]
DEBUG:root:TICK:  479 PC:   21 AR:   20 Z_FLAG: 0       {'opcode': 'save'}                      DATA_STACK: [10, 108, 101]
DEBUG:root:TICK:  486 PC:   22 AR:  101 Z_FLAG: 0       {'opcode': 'inc'}                       DATA_STACK: [10]
DEBUG:root:TICK:  490 PC:   23 AR:   22 Z_FLAG: 0       {'opcode': 'push', 'arg': 15}           DATA_STACK: [11] 
DEBUG:root:TICK:  494 PC:   24 AR:   23 Z_FLAG: 0       {'opcode': 'jump'}                      DATA_STACK: [11, 15]
DEBUG:root:TICK:  499 PC:   15 AR:   24 Z_FLAG: 0       {'opcode': 'dup'}                       DATA_STACK: [11]
DEBUG:root:TICK:  503 PC:   16 AR:   15 Z_FLAG: 0       {'opcode': 'load'}                      DATA_STACK: [11, 11]
DEBUG:root:TICK:  508 PC:   17 AR:   11 Z_FLAG: 0       {'opcode': 'test'}                      DATA_STACK: [11, 100]
DEBUG:root:TICK:  512 PC:   18 AR:   17 Z_FLAG: 0       {'opcode': 'push', 'arg': 25}           DATA_STACK: [11, 100]
DEBUG:root:TICK:  516 PC:   19 AR:   18 Z_FLAG: 0       {'opcode': 'jz'}                        DATA_STACK: [11, 100, 25]
DEBUG:root:TICK:  520 PC:   20 AR:   19 Z_FLAG: 0       {'opcode': 'push', 'arg': 101}          DATA_STACK: [11, 100]
DEBUG:root:TICK:  524 PC:   21 AR:   20 Z_FLAG: 0       {'opcode': 'save'}                      DATA_STACK: [11, 100, 101]
DEBUG:root:TICK:  531 PC:   22 AR:  101 Z_FLAG: 0       {'opcode': 'inc'}                       DATA_STACK: [11]
DEBUG:root:TICK:  535 PC:   23 AR:   22 Z_FLAG: 0       {'opcode': 'push', 'arg': 15}           DATA_STACK: [12]
DEBUG:root:TICK:  539 PC:   24 AR:   23 Z_FLAG: 0       {'opcode': 'jump'}                      DATA_STACK: [12, 15]
DEBUG:root:TICK:  544 PC:   15 AR:   24 Z_FLAG: 0       {'opcode': 'dup'}                       DATA_STACK: [12]
DEBUG:root:TICK:  548 PC:   16 AR:   15 Z_FLAG: 0       {'opcode': 'load'}                      DATA_STACK: [12, 12]
DEBUG:root:TICK:  553 PC:   17 AR:   12 Z_FLAG: 0       {'opcode': 'test'}                      DATA_STACK: [12, 33]
DEBUG:root:TICK:  557 PC:   18 AR:   17 Z_FLAG: 0       {'opcode': 'push', 'arg': 25}           DATA_STACK: [12, 33]
DEBUG:root:TICK:  561 PC:   19 AR:   18 Z_FLAG: 0       {'opcode': 'jz'}                        DATA_STACK: [12, 33, 25]
DEBUG:root:TICK:  565 PC:   20 AR:   19 Z_FLAG: 0       {'opcode': 'push', 'arg': 101}          DATA_STACK: [12, 33]
DEBUG:root:TICK:  569 PC:   21 AR:   20 Z_FLAG: 0       {'opcode': 'save'}                      DATA_STACK: [12, 33, 101]
DEBUG:root:TICK:  576 PC:   22 AR:  101 Z_FLAG: 0       {'opcode': 'inc'}                       DATA_STACK: [12]
DEBUG:root:TICK:  580 PC:   23 AR:   22 Z_FLAG: 0       {'opcode': 'push', 'arg': 15}           DATA_STACK: [13]
DEBUG:root:TICK:  584 PC:   24 AR:   23 Z_FLAG: 0       {'opcode': 'jump'}                      DATA_STACK: [13, 15]
DEBUG:root:TICK:  589 PC:   15 AR:   24 Z_FLAG: 0       {'opcode': 'dup'}                       DATA_STACK: [13]
DEBUG:root:TICK:  593 PC:   16 AR:   15 Z_FLAG: 0       {'opcode': 'load'}                      DATA_STACK: [13, 13]
DEBUG:root:TICK:  598 PC:   17 AR:   13 Z_FLAG: 0       {'opcode': 'test'}                      DATA_STACK: [13, 0]
DEBUG:root:TICK:  602 PC:   18 AR:   17 Z_FLAG: 1       {'opcode': 'push', 'arg': 25}           DATA_STACK: [13, 0]
DEBUG:root:TICK:  606 PC:   19 AR:   18 Z_FLAG: 1       {'opcode': 'jz'}                        DATA_STACK: [13, 0, 25]
DEBUG:root:TICK:  611 PC:   25 AR:   19 Z_FLAG: 0       {'opcode': 'halt'}                      DATA_STACK: [13, 0]
INFO:root:output buffer: Hello, world!
output: Hello, world!
instr_counter:  136 ticks:  614
```

| ФИО                        | <алг>       | <LoC> | <code байт> | <code инстр.> | <инстр.> | <такт.> | <вариант>    |
|----------------------------|-------------|-------|-------------|---------------|----------|---------|--------------|
| Сиразетдинов Азат Ниязович | hello_world | 17    | -           | 13            | 138      | 611     |asm \| stack \| neum \| hw \| tick -> instr \| struct \| trap -> stream \| mem \| cstr \| prob1 \| spi |
| Сиразетдинов Азат Ниязович | cat         | 15    | -           | 12            | 152      | 682     |asm \| stack \| neum \| hw \| tick -> instr \| struct \| trap -> stream \| mem \| cstr \| prob1 \| spi |
| Сиразетдинов Азат Ниязович | hello       | 75    | -           | 60            | 447      | 2056    |asm \| stack \| neum \| hw \| tick -> instr \| struct \| trap -> stream \| mem \| cstr \| prob1 \| spi |
| Сиразетдинов Азат Ниязович | prob1       | 52    | -           | 42            | 40       | 187     |asm \| stack \| neum \| hw \| tick -> instr \| struct \| trap -> stream \| mem \| cstr \| prob1 \| spi |
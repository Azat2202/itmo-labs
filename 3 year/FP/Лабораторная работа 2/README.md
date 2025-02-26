
# Лабораторная работа 2 по Функциональному программированию

## Требования к разработанному ПО

Язык: idris2

Вариант: oa-bag

Выполнил: Сиразетдинов Азат Ниязович 368796

Дополнительное задание: реализовать структуру данных с доказательством наличия элемента

## Ключевые элементы реализации

Интерфейс Hashable для элемента списка

Структура данных Entry для элемента списка содержит либо пустое значение либо элемент и количество

Структура данных HashMap содержит количество элементов и вектор Entry

Основное API:

- insert - возвращает список. Если кончилось место, то расширяет список
- delete - возвращает объект Maybe
- find - возвращает объект Maybe
- filter
- merge
- map - перехеширует список
- foldr
- foldl
- show

Дополнительное задание реализовано в виде словаря с доказательством наличия элемента по ключу

Тесты находятся в файле DictElemUnitTests.idr

## Тесты

Программа протестирована юнит-тестированием и тестированием свойств

В юнит тестировании проверены все методы и характеристика multiset

В тестировании свойств проверены свойства моноида и коммутативность сложения

Отчет инструмента тестирования:

```
creation of hashMap size k: test passed
creation of hashmap size Z: test passed
inserting value without shrinking: test passed
inserting value with repetition without shrinking: test passed
insert all from vect test: test passed
insert with shrinking: test passed
check hashing: test passed
delete found repeated value: test passed
delete found not repeated value: test passed
delete not found value: test passed
find found at first hash: test passed
find found at second hash: test passed
find not found: test passed
filter test: test passed
map with hash test: test passed
foldr test: test passed
foldl test: test passed
0 test(s) failed
━━━ Property tests ━━━
  ✓ (hm1 <+> hm2) <+> hm3 == hm1 <+> (hm2 <+> hm3) passed 100 tests.
  ✓ e <+> hm1 == hm1 == hm1 <+> e passed 100 tests.
  ✓ hm1 <+> hm2 === hm2 <+> hm1 passed 100 tests.
  ✓ 3 succeeded.
```

## Выводы

Лабораторная работа заняла ГОРАЗДО больше времени чем я предполагал и разрабатывать ПО на языке idris оказалось очень трудоемко.
К сожалению, реализовать идеальную структуру данных (с доказательством наличия пустой ячейки в списке) заняло бы чресчур много времени и уже сильно выходит из требований лабораторной работы.
Но текущим результатом я доволен, потому что теперь много нового понимаю в этом ЯП

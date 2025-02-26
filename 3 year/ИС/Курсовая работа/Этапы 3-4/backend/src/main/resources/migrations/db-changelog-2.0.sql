INSERT INTO body_type (name, level)
VALUES
    ('Атлетическое', 1),
    ('Обычное', 0),
    ('Полное', -1);


INSERT INTO profession (name, level)
VALUES
    ('Врач', 1),
    ('Учитель', 0),
    ('Инженер', -1);


INSERT INTO trait (name, level)
VALUES
    ('Храбрость', 1),
    ('Честность', 0),
    ('Осторожность', -1);


INSERT INTO phobia (name, level)
VALUES
    ('Арахнофобия', 1),
    ('Клаустрофобия', 0),
    ('Высота', -1);


INSERT INTO health (name, level)
VALUES
    ('Здоров', 1),
    ('Проблемы со зрением', 0),
    ('Диабет', -1);


INSERT INTO bag (name, level)
VALUES
    ('Небольшая аптечка', 1),
    ('Рюкзак с провизией', 0),
    ('Набор инструментов', -1);


INSERT INTO hobby (name, level)
VALUES
    ('Чтение', 1),
    ('Плавание', 0),
    ('Рисование', -1);


INSERT INTO equipment (name, level)
VALUES
    ('Аптечка', 1),
    ('Пистолет', 0),
    ('Геологический молоток', -1);


INSERT INTO bunker (square, stay_days, food_days)
VALUES
    (50, 10, 15),
    ( 100, 20, 30),
    ( 75, 5, 10);


INSERT INTO cataclysm (description)
VALUES
    ('Зомби-апокалипсис'),
    ( 'Глобальное потепление'),
    ('Ядерная война');

CREATE TYPE MOOD AS ENUM(
	'стремглав',
	'как ни старался'
);

CREATE TYPE STATE_SIZE AS ENUM(
	'уменьшенная копия',
	'полноразмерный город'
);

CREATE TYPE ROAD_LOCATION AS ENUM (
	'сквозь пласты земли'
);

CREATE TYPE SPEED AS ENUM (
	'стремительно'
);

CREATE TABLE IF NOT EXISTS city(
	id SERIAL NOT NULL PRIMARY KEY,
	city_name TEXT NOT NULL,
	city_size STATE_SIZE
);

CREATE TABLE IF NOT EXISTS road(
	id SERIAL NOT NULL PRIMARY KEY,
	endpoint_id INT NOT NULL REFERENCES city(id),
	road_location ROAD_LOCATION
);

CREATE TABLE IF NOT EXISTS car(
	id SERIAL NOT NULL PRIMARY KEY,
	road_id int NOT NULL REFERENCES road(id),
	speed SPEED NOT null
);;

CREATE TABLE IF NOT EXISTS human(
	id SERIAL PRIMARY KEY,
	car_id INT REFERENCES car(id),
	human_name text not NULL
);

CREATE TABLE IF NOT EXISTS imagination(
	id SERIAL PRIMARY KEY,
	human_id INT NOT NULL REFERENCES human(id),
	city_id INT REFERENCES city(id),
	imagination_text TEXT,
	mood MOOD
);

CREATE TABLE IF NOT EXISTS question(
	id SERIAL PRIMARY KEY,
	imagination_id INT NOT NULL REFERENCES imagination(id),
	question_text TEXT NOT null
);

--Его воображение стремглав уносилось к Лизу, 
--словно торопясь прибыть туда ранее тела. 
--Что же это будет за город? 
--Как ни старался
--Олвин, он мог представить себе всего только 
--уменьшенную копию Диаспара. Да и существует 
--ли он еще? -- думалось ему... Но он быстро 
--убедил себя, что, будь иначе, машина не несла 
--бы его с такой стремительностью сквозь пласты земли.

INSERT INTO city(city_name, city_size)
	VALUES
		('Лиз', 'полноразмерный город'),
		('Диаспар', 'уменьшенная копия');


INSERT INTO road(endpoint_id, road_location)
	VALUES
		(1, 'сквозь пласты земли');


INSERT INTO car (road_id, speed)
	VALUES (1, 'стремительно');

INSERT INTO human (car_id, human_name)
	VALUES (1, 'Олвин');

INSERT INTO imagination(human_id, city_id, imagination_text, "mood")
VALUES
	(1, 1, 'прибыть ранее тела', 'стремглав'),
	(1, 1, NULL ,'как ни старался');

INSERT INTO question (imagination_id, question_text)
	VALUES
		(1, 'Что же это будет за город?'),
		(2, 'Да и существует ли он еще?');

-- Дополнительное задание
-- Вывести скорость машины на которой Олвин едет в Лиз

SELECT speed FROM car
WHERE road_id = (
    SELECT id FROM road
    WHERE endpoint_id = (
        SELECT id FROM city
        WHERE (city_name = 'Лиз')
    )
      AND
            id = (
            SELECT car_id FROM human
            WHERE (human_name = 'Олвин')
        )
);

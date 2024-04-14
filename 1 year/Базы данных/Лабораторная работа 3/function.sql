INSERT INTO road(endpoint_id, road_location)
	VALUES
		(1, 'сквозь пласты земли'),
		(1, 'сквозь пласты земли'),
		(1, 'сквозь пласты земли');
	
INSERT INTO car (road_id, speed)
	VALUES 
	(1, 'стремительно'),
	(1, 'стремительно'),
	(2, 'стремительно'),
	(2, 'стремительно'),
	(2, 'стремительно'),
	(2, 'стремительно'),
	(2, 'стремительно'),
	(2, 'стремительно'),
	(3, 'стремительно'),
	(3, 'стремительно'),
	(3, 'стремительно'),
	(3, 'стремительно'),
	(3, 'стремительно'),
	(3, 'стремительно'),
	(3, 'стремительно'),
	(3, 'стремительно');


-- функция
CREATE OR REPLACE  FUNCTION low_traffic_road_id() RETURNS integer AS $$
DECLARE
    min_road integer;
BEGIN
	IF (EXISTS (
		SELECT * 
		FROM road 
		LEFT JOIN car 
		ON car.road_id = road.id
		WHERE car.id IS NULL
	)) THEN
		SELECT road.id INTO min_road
		FROM road 
		LEFT JOIN car 
		ON car.road_id = road.id
		WHERE car.id IS NULL
		LIMIT 1;
		RETURN min_road;
    ELSE
	    SELECT road_id INTO min_road
	    FROM car 
	    GROUP BY car.road_id 
		ORDER BY COUNT(*)
	    LIMIT 1;
	    RETURN min_road;
	END IF;
END;
$$
LANGUAGE plpgsql;

SELECT * FROM low_traffic_road_id();


-- триггер
CREATE OR REPLACE FUNCTION update_road_traffic()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'INSERT') THEN
        UPDATE road
        SET traffic_load = traffic_load + 1
        WHERE id = NEW.road_id;
    ELSIF (TG_OP = 'DELETE') THEN
        UPDATE road
        SET traffic_load = traffic_load - 1
        WHERE id = OLD.road_id;
	ELSIF (TG_OP = 'UPDATE') THEN
        UPDATE road
        SET traffic_load = traffic_load - 1
        WHERE id = OLD.road_id;
		UPDATE road
        SET traffic_load = traffic_load + 1
        WHERE id = NEW.road_id;

    END IF;
    RETURN NULL;
END
$$
LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER update_road_traffic_trigger
AFTER INSERT OR DELETE OR UPDATE ON car 
FOR EACH ROW 
EXECUTE FUNCTION update_road_traffic();

SELECT * FROM road;







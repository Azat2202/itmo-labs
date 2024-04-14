package managers;

public class DatabaseCommands {
    public static final String allTablesCreation = """
            CREATE TYPE FORM_OF_EDUCATION AS ENUM(
                'DISTANCE_EDUCATION',
                'FULL_TIME_EDUCATION',
                'EVENING_CLASSES'
            );
            CREATE TYPE COLOR AS ENUM (
                'GREEN',
                'RED',
                'ORANGE',
                'WHITE',
                'BROWN'
            );
            CREATE TYPE COUNTRY AS ENUM(
                'UNITED_KINGDOM',
                'FRANCE',
                'CHINA',
                'INDIA',
                'SOUTH_KOREA'
            );
            CREATE TABLE IF NOT EXISTS studyGroup (
                id SERIAL PRIMARY KEY,
                group_name TEXT NOT NULL ,
                cord_x NUMERIC NOT NULL,
                cord_y NUMERIC NOT NULL ,
                creation_date TIMESTAMP NOT NULL ,
                students_count BIGINT NOT NULL ,
                expelled_students BIGINT NOT NULL ,
                average_mark BIGINT NOT NULL ,
                form_of_education FORM_OF_EDUCATION,
                person_name TEXT NOT NULL ,
                person_weight INT NOT NULL ,
                person_eye_color COLOR,
                person_hair_color COLOR,
                person_nationality COUNTRY,
                person_location_x BIGINT NOT NULL,
                person_location_y BIGINT NOT NULL,
                person_location_name TEXT NOT NULL,
                owner_login TEXT NOT NULL
            );
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                login TEXT,
                password TEXT,
                salt TEXT
            );
            """;
    public static final String addUser = """
            INSERT INTO users(login, password, salt) VALUES (?, ?, ?);""";

    public static final String getUser = """
            SELECT * FROM users WHERE (login = ?);""";

    public static final String addObject = """
            INSERT INTO studygroup(group_name, cord_x, cord_y, creation_date, students_count, expelled_students, average_mark, form_of_education, person_name, person_weight, person_eye_color, person_hair_color, person_nationality, person_location_x, person_location_y, person_location_name, owner_login)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id;
            """;

    public static final String getAllObjects = """
            SELECT * FROM studygroup;
            """;

    public static final String deleteUserOwnedObjects = """
            DELETE FROM studygroup WHERE (owner_login = ?) AND (id = ?) RETURNING id;
            """;

    public static final String deleteUserObject = """
            DELETE FROM studygroup WHERE (owner_login = ?) AND (id = ?) RETURNING id;
            """;

    public static final String updateUserObject = """
            UPDATE studygroup
            SET (group_name, cord_x, cord_y, creation_date, students_count, expelled_students, average_mark, form_of_education, person_name, person_weight, person_eye_color, person_hair_color, person_nationality, person_location_x, person_location_y, person_location_name)
             = (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            WHERE (id = ?) AND (owner_login = ?)
            RETURNING id;
            """;
}

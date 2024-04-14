package database;

public class DatabaseCommands {
    public static final String createTable = """
            CREATE TABLE IF NOT EXISTS points(
                id SERIAL PRIMARY KEY,
                x REAL NOT NULL,
                y REAL NOT NULL,
                r REAL NOT NULL,
                success BOOLEAN NOT NULL,
                cur_time TEXT NOT NULL,
                execution_time TEXT NOT NULL
            );
            """;

    public static final String addPoint = """
            INSERT INTO points(x, y, r, success, cur_time, execution_time)
            VALUES (?, ?, ?, ?, ?, ?);""";

    public static final String getPoints = """
            SELECT * FROM points;
            """;

    public static final String truncate = """
            TRUNCATE TABLE points;
            """;
}

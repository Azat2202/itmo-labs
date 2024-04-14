package utility;

import managers.DatabaseManager;

import java.util.Objects;

/**
 * Класс является реализацией паттерна программирования синглтон, и нужен для обращения
 * к единственному менеджеру базы данных
 */
public class DatabaseHandler {
    private static DatabaseManager databaseManager;
    static {
        databaseManager = new DatabaseManager();
    }
    public static DatabaseManager getDatabaseManager(){
        if (Objects.isNull(databaseManager)) databaseManager = new DatabaseManager();
        return databaseManager;
    }
}

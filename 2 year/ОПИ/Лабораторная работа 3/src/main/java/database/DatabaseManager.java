package database;

import models.Point;
import secrets.DatabaseAccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class DatabaseManager {
    private Connection connection;
    private final String user = DatabaseAccess.USER;
    private final String password = DatabaseAccess.PASSWORD;
    private final String DATABASE_URL = "jdbc:postgresql://localhost:5432/studs";
    public final String DATABASE_URL_HELIOS = "jdbc:postgresql://pg:5432/studs";

    public DatabaseManager(){
        try {
            this.connect();
            this.createMainBase();
        } catch (SQLException e) {
            System.err.println("Ошибка при исполнени изначального запроса либо таблицы уже созданы");
        }
    }

    public void connect(){
        Properties info = null;
        try {
            connection = DriverManager.getConnection(DATABASE_URL, user, password);
            System.out.println("подключение успешно");
        } catch (SQLException e) {
            try{
                connection = DriverManager.getConnection(DATABASE_URL_HELIOS, user, password);
            } catch (SQLException ex) {
                System.err.println("Невозможно подключиться к базе данных");
                e.printStackTrace();
                ex.printStackTrace();
                System.exit(1);
            }
        }
    }

    public void createMainBase() throws SQLException {
        try {
            connection.prepareStatement(DatabaseCommands.createTable).execute();
            System.out.println("таблица создана");
        } catch (SQLException e) {
            System.out.println("aaaaaaaaaaaaaa");
            e.printStackTrace();
        }
    }

    public void addPoint(Point point){
        try {
            PreparedStatement ps = connection.prepareStatement(DatabaseCommands.addPoint);
            ps.setDouble(1, point.getX());
            ps.setDouble(2, point.getY());
            ps.setDouble(3, point.getR());
            ps.setBoolean(4, point.getSuccess());
            ps.setString(5, point.getCurrentTime());
            ps.setString(6, point.getExecutionTime());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Point> loadCollection(){
        try {
            PreparedStatement ps = connection.prepareStatement(DatabaseCommands.getPoints);
            ResultSet resultSet = ps.executeQuery();
            ArrayList<Point> collection = new ArrayList<>();
            while (resultSet.next()){
                collection.add(0, new Point(
                        resultSet.getLong("id"),
                        resultSet.getDouble("x"),
                        resultSet.getDouble("y"),
                        resultSet.getDouble("r"),
                        resultSet.getBoolean("success"),
                        resultSet.getString("cur_time"),
                        resultSet.getString("execution_time")
                ));
            }
            System.out.println("коллекция загружена");
            return collection;
        } catch (SQLException e) {
            System.err.println("Коллекция пуста либо возникла ошибка при исполнении запроса");
            return new ArrayList<>();
        }
    }


    public void clearCollection() {
        try {
            connection.prepareStatement(DatabaseCommands.truncate).execute();
        } catch (SQLException e) {
            System.err.println(e.getErrorCode());
        }
    }
}

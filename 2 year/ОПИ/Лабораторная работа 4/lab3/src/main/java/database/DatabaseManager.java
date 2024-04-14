package database;

import models.Point;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class DatabaseManager {
    private Connection connection;
    private final String user = "...";
    private final String password = "...";
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
        connection
                .prepareStatement(DatabaseCommands.createTable)
                .execute();
    }

    public void addPoint(Point point){
        try {
            PreparedStatement ps = connection.prepareStatement(DatabaseCommands.addPoint);
            ps.setFloat(1, point.getX());
            ps.setFloat(2, point.getY());
            ps.setFloat(3, point.getR());
            ps.setBoolean(4, point.getStatus());
            ps.setString(5, point.getTime());
            ps.setLong(6, point.getScriptTime());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public LinkedList<Point> loadCollection(){
        try {
            PreparedStatement ps = connection.prepareStatement(DatabaseCommands.getPoints);
            ResultSet resultSet = ps.executeQuery();
            LinkedList<Point> collection = new LinkedList<>();
            while (resultSet.next()){
                collection.add(new Point(
                        resultSet.getLong("id"),
                        resultSet.getFloat("x"),
                        resultSet.getFloat("y"),
                        resultSet.getFloat("r"),
                        resultSet.getBoolean("status"),
                        resultSet.getString("time"),
                        resultSet.getLong("script_time")
                ));
            }
            return collection;
        } catch (SQLException e) {
            System.err.println("Коллекция пуста либо возникла ошибка при исполнении запроса");
            return new LinkedList<>();
        }
    }


    public void clearCollection() throws SQLException {
        connection
                .prepareStatement(DatabaseCommands.truncate)
                .execute();
    }
}

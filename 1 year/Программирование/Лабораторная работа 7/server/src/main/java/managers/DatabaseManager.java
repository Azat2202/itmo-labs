package managers;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

import dtp.User;
import main.App;
import models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseManager {
    private Connection connection;
    private MessageDigest md;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrs" +
            "tuvwxyz0123456789<>?:@{!$%^&*()_+£$";
    private static final String PEPPER = "[g$J*(l;";
    private static final Logger databaseLogger = LogManager.getLogger(DatabaseManager.class);

    public DatabaseManager(){
        try {
            md = MessageDigest.getInstance(App.HASHING_ALGORITHM);

            this.connect();
            this.createMainBase();
        } catch (SQLException e) {
            databaseLogger.warn("Ошибка при исполнени изначального запроса либо таблицы уже созданы");
        } catch (NoSuchAlgorithmException e) {
            databaseLogger.fatal("Такого алгоритма нет!");
        }
    }

    public void connect(){
        Properties info = null;
        try {
            info = new Properties();
            info.load(new FileInputStream(App.DATABASE_CONFIG_PATH));
            connection = DriverManager.getConnection(App.DATABASE_URL, info);
            databaseLogger.info("Успешно подключен к базе данных");
        } catch (SQLException | IOException e) {
            try{

                connection = DriverManager.getConnection(App.DATABASE_URL_HELIOS, info);
            } catch (SQLException ex) {
                databaseLogger.fatal("Невозможно подключиться к базе данных");
                databaseLogger.debug(e);
                databaseLogger.debug(ex);
                System.exit(1);
            }
        }
    }

    public void createMainBase() throws SQLException {
        connection
                .prepareStatement(DatabaseCommands.allTablesCreation)
                .execute();
        databaseLogger.info("Таблицы созданы");
    }

    public void addUser(User user) throws SQLException {
        String login = user.name();
        String salt = this.generateRandomString();
        String pass = PEPPER + user.password() + salt;

        PreparedStatement ps = connection.prepareStatement(DatabaseCommands.addUser);
        if (this.checkExistUser(login)) throw new SQLException();
        ps.setString(1, login);
        ps.setString(2, this.getSHA512Hash(pass));
        ps.setString(3, salt);
        ps.execute();
        databaseLogger.info("Добавлен юзер " + user);
    }

    public boolean confirmUser(User inputUser){
        try {
            String login = inputUser.name();
            PreparedStatement getUser = connection.prepareStatement(DatabaseCommands.getUser);
            getUser.setString(1, login);
            ResultSet resultSet = getUser.executeQuery();
            if(resultSet.next()) {
                String salt = resultSet.getString("salt");
                String toCheckPass = this.getSHA512Hash(PEPPER + inputUser.password() + salt);
                return toCheckPass.equals(resultSet.getString("password"));
            }
            else {
                return false;
            }
        } catch (SQLException e) {
            databaseLogger.fatal("Неверная команда sql!");
            databaseLogger.debug(e);
            return false;
        }
    }

    public boolean checkExistUser(String login) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(DatabaseCommands.getUser);
        ps.setString(1, login);
        ResultSet resultSet = ps.executeQuery();
        return resultSet.next();
    }

    // Метод возвращает -1 при ошибке добавления объекта
    public int addObject(StudyGroup studyGroup, User user){
        try {
            PreparedStatement ps = connection.prepareStatement(DatabaseCommands.addObject);
            ps.setString(1, studyGroup.getName());
            ps.setFloat(2, studyGroup.getCoordinates().getX());
            ps.setDouble(3, studyGroup.getCoordinates().getY());
            ps.setTimestamp(4, new java.sql.Timestamp(studyGroup.getCreationDate().getTime()));
            ps.setLong(5, studyGroup.getStudentsCount());
            ps.setLong(6, studyGroup.getExpelledStudents());
            ps.setLong(7, studyGroup.getAverageMark());
            ps.setObject(8, studyGroup.getFormOfEducation(), Types.OTHER);
            ps.setString(9, studyGroup.getGroupAdmin().getName());
            ps.setInt(10, studyGroup.getGroupAdmin().getWeight());
            ps.setObject(11, studyGroup.getGroupAdmin().getEyeColor(), Types.OTHER);
            ps.setObject(12, studyGroup.getGroupAdmin().getHairColor(), Types.OTHER);
            ps.setObject(13, studyGroup.getGroupAdmin().getNationality(), Types.OTHER);
            ps.setDouble(14, studyGroup.getGroupAdmin().getLocation().getX());
            ps.setDouble(15, studyGroup.getGroupAdmin().getLocation().getY());
            ps.setString(16, studyGroup.getGroupAdmin().getLocation().getName());
            ps.setString(17, user.name());
            ResultSet resultSet = ps.executeQuery();

            if (!resultSet.next()) {
                databaseLogger.info("Объект не добавлен в таблицу");
                return -1;
            }
            databaseLogger.info("Объект добавлен в таблицу");
            return resultSet.getInt(1);
        } catch (SQLException e) {
            databaseLogger.info("Объект не добавлен в таблицу");
            databaseLogger.debug(e);
            return -1;
        }
    }

    public boolean updateObject(int id, StudyGroup studyGroup, User user){
        try {
            PreparedStatement ps = connection.prepareStatement(DatabaseCommands.updateUserObject);
            ps.setString(1, studyGroup.getName());
            ps.setFloat(2, studyGroup.getCoordinates().getX());
            ps.setDouble(3, studyGroup.getCoordinates().getY());
            ps.setTimestamp(4, new java.sql.Timestamp(studyGroup.getCreationDate().getTime()));
            ps.setLong(5, studyGroup.getStudentsCount());
            ps.setLong(6, studyGroup.getExpelledStudents());
            ps.setLong(7, studyGroup.getAverageMark());
            ps.setObject(8, studyGroup.getFormOfEducation(), Types.OTHER);
            ps.setString(9, studyGroup.getGroupAdmin().getName());
            ps.setInt(10, studyGroup.getGroupAdmin().getWeight());
            ps.setObject(11, studyGroup.getGroupAdmin().getEyeColor(), Types.OTHER);
            ps.setObject(12, studyGroup.getGroupAdmin().getHairColor(), Types.OTHER);
            ps.setObject(13, studyGroup.getGroupAdmin().getNationality(), Types.OTHER);
            ps.setDouble(14, studyGroup.getGroupAdmin().getLocation().getX());
            ps.setDouble(15, studyGroup.getGroupAdmin().getLocation().getY());
            ps.setString(16, studyGroup.getGroupAdmin().getLocation().getName());

            ps.setInt(17, id);
            ps.setString(18, user.name());
            ResultSet resultSet = ps.executeQuery();
            System.out.println(resultSet);
            return resultSet.next();
        } catch (SQLException e) {
            databaseLogger.debug(e);
            return false;
        }
    }

    public boolean deleteObject(int id, User user){
        try{
            PreparedStatement ps = connection.prepareStatement(DatabaseCommands.deleteUserObject);
            ps.setString(1, user.name());
            ps.setInt(2, id);
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            databaseLogger.error("Объект удалить не удалось");
            databaseLogger.debug(e);
            return false;
        }
    }

    public boolean deleteAllObjects(User user, List<Integer> ids){
        try {
            for (Integer id : ids) {
                PreparedStatement ps = connection.prepareStatement(DatabaseCommands.deleteUserOwnedObjects);
                ps.setString(1, user.name());
                ps.setInt(2, id);
                ResultSet resultSet = ps.executeQuery();
            }
            databaseLogger.warn("Удалены все строки таблицы studygroup принадлежащие " + user.name());
            return true;
        } catch (SQLException e) {
            databaseLogger.error("Удалить строки таблицы studygroup не удалось!");
            databaseLogger.debug(e);
            return false;
        }
    }

    public ArrayDeque<StudyGroup> loadCollection(){
        try {
            PreparedStatement ps = connection.prepareStatement(DatabaseCommands.getAllObjects);
            ResultSet resultSet = ps.executeQuery();
            ArrayDeque<StudyGroup> collection = new ArrayDeque<>();
            while (resultSet.next()){
                collection.add(new StudyGroup(
                        resultSet.getInt("id"),
                        resultSet.getString("group_name"),
                        new Coordinates(
                                resultSet.getFloat("cord_x"),
                            resultSet.getDouble("cord_y")
                        ),
                        resultSet.getTimestamp("creation_date"),
                        resultSet.getLong("students_count"),
                        resultSet.getLong("expelled_students"),
                        resultSet.getLong("average_mark"),
                        FormOfEducation.valueOf(resultSet.getString("form_of_education")),
                        new Person(
                            resultSet.getString("person_name"),
                            resultSet.getInt("person_weight"),
                            Color.valueOf(resultSet.getString("person_eye_color")),
                            Color.valueOf(resultSet.getString("person_hair_color")),
                            Country.valueOf(resultSet.getString("person_nationality")),
                            new Location(
                                    resultSet.getDouble("person_location_x"),
                                    resultSet.getLong("person_location_y"),
                                    resultSet.getString("person_location_name")
                            )
                        ),
                        resultSet.getString("owner_login")
                ));
            }
            databaseLogger.info("Коллекция успешно загружена из таблицы");
            return collection;
        } catch (SQLException e) {
            databaseLogger.warn("Коллекция пуста либо возникла ошибка при исполнении запроса");
            return new ArrayDeque<>();
        }
    }

    private String generateRandomString() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    private String getSHA512Hash(String input){
        byte[] inputBytes = input.getBytes();
        md.update(inputBytes);
        byte[] hashBytes = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

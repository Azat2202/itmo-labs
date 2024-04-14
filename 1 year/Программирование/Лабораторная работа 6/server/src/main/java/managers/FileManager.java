package managers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.security.AnyTypePermission;
import exceptions.ExitObliged;
import exceptions.InvalidForm;
import models.StudyGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilty.Console;
import utilty.ConsoleColors;
import utilty.Printable;
import utilty.Server;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Класс реализующий работу с файлами
 * @author azat2202
 */
public class FileManager {
    private String text;
    private final Printable console;
    private final CollectionManager collectionManager;
    private final XStream xStream = new XStream();

    static final Logger fileManagerLogger = LogManager.getLogger(FileManager.class);


    /**
     * В конструкторе задаются алиасы для библиотеки {@link XStream} которая используется для работы с xml
     * @param console Пользовательский ввод-вывод
     * @param collectionManager Работа с коллекцией
     */
    public FileManager(Printable console, CollectionManager collectionManager) {
        this.console = console;
        this.collectionManager = collectionManager;

        this.xStream.alias("StudyGroup", StudyGroup.class);
        this.xStream.alias("Array", CollectionManager.class);
        this.xStream.addPermission(AnyTypePermission.ANY);
        this.xStream.addImplicitCollection(CollectionManager.class, "collection");
        fileManagerLogger.info("Созданы алиасы для xstream");
    }

    /**
     * Обращение к переменным среды и чтение файла в поле по указанному пути
     * @throws ExitObliged если путь - null или отсутствует программа заканчивает выполнение
     */
    public void findFile() throws ExitObliged{
        String file_path = System.getenv("file_path");
        if (file_path == null || file_path.isEmpty()) {
            console.printError("Путь должен быть в переменных окружения в переменной 'file_path'");
            fileManagerLogger.fatal("Нет пути в переменных окружения");
            throw new ExitObliged();
        }
        else console.println(ConsoleColors.toColor("Путь получен успешно", ConsoleColors.PURPLE));
        fileManagerLogger.info("Путь получен успешно");

        File file = new File(file_path);
        BufferedInputStream bis;
        FileInputStream fis;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            fileManagerLogger.info("Отрыто соединение с файлом");
            while (bis.available() > 0) {
                stringBuilder.append((char) bis.read());
            }
            fis.close();
            bis.close();
            fileManagerLogger.info("Файл прочитан");
            if (stringBuilder.isEmpty()) {
                console.printError("Файл пустой");
                fileManagerLogger.info("Файл пустой");
                this.text = "</Array>";
                return;
            }
            this.text = stringBuilder.toString();
        } catch (FileNotFoundException fnfe) {
            console.printError("Такого файла не найдено");
            fileManagerLogger.fatal("Такого файла не найдено");
            throw new ExitObliged();
        } catch (IOException ioe) {
            console.printError("Ошибка ввода/вывода" + ioe);
            fileManagerLogger.fatal("Ошибка ввода/вывода" + ioe);
            throw new ExitObliged();
        }
    }

    /**
     * Создание объектов в консольном менеджере
     * @throws ExitObliged Если объекты в файле невалидны выходим из программы
     */
    public void createObjects() throws ExitObliged{
        try{
            XStream xstream = new XStream();
            xstream.alias("StudyGroup", StudyGroup.class);
            xstream.alias("Array", CollectionManager.class);
            xstream.addPermission(AnyTypePermission.ANY);
            xstream.addImplicitCollection(CollectionManager.class, "collection");
            fileManagerLogger.info("Сконфигурирован xstream для чтения из файла");
            CollectionManager collectionManagerWithObjects = (CollectionManager) xstream.fromXML(this.text);
            fileManagerLogger.info("Прочитан файл", collectionManagerWithObjects.getCollection());
            for(StudyGroup s : collectionManagerWithObjects.getCollection()){
                if (this.collectionManager.checkExist(s.getId())){
                    console.printError("В файле повторяются айди!");
                    fileManagerLogger.fatal("В файле повторяются айди!");
                    throw new ExitObliged();
                }
                if (!s.validate()) throw new InvalidForm();
                this.collectionManager.addElement(s);
                fileManagerLogger.info("Добавлен объект", s);
            }
        } catch (InvalidForm invalidForm) {
            console.printError("Объекты в файле не валидны");
            fileManagerLogger.fatal("Объекты в файле не валидны");
            throw new ExitObliged();
        } catch (StreamException streamException){
            console.println("Файл пустой");
            fileManagerLogger.error("Файл пустой");
        }
        console.println("Получены объекты:\n" + collectionManager.getCollection().toString());
        CollectionManager.updateId(collectionManager.getCollection());
    }

    /**
     * Сохраняем коллекцию из менеджера в файл
     */
    public void saveObjects(){
        String file_path = System.getenv("file_path");
        if (file_path == null || file_path.isEmpty()) {
            console.printError("Путь должен быть в переменных окружения в перменной 'file_path'");
            fileManagerLogger.fatal("Отсутствует путь в переменных окружения");
        }
        else {
            console.println(ConsoleColors.toColor("Путь получен успешно", ConsoleColors.PURPLE));
            fileManagerLogger.info(ConsoleColors.toColor("Путь получен успешно", ConsoleColors.PURPLE));
        }

        try{
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file_path));
            out.write(this.xStream.toXML(collectionManager)
                    .getBytes(StandardCharsets.UTF_8));
            out.close();
            fileManagerLogger.info("Файл записан");
        } catch (FileNotFoundException e) {
            console.printError("Файл не существует");
            fileManagerLogger.error("Файл не существует");
        }catch (IOException e){
            console.printError("Ошибка ввода вывода");
            fileManagerLogger.error("Ошибка ввода вывода");
        }
    }
}
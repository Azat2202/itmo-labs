package managers;

import exceptions.InvalidForm;
import models.StudyGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

/**
 * Класс организующий работу с коллекцией
 */
public class CollectionManager {
    private final ArrayDeque<StudyGroup> collection = new ArrayDeque<>();
    private static int nextId = 0;
    /**
     * Дата создания коллекции
     */
    private LocalDateTime lastInitTime;
    /**
     * Дата последнего изменения коллекции
     */
    private LocalDateTime lastSaveTime;

    static final Logger collectionManagerLogger = LogManager.getLogger(CollectionManager.class);

    public CollectionManager() {
        this.lastInitTime = LocalDateTime.now();
        this.lastSaveTime = null;
    }

    public ArrayDeque<StudyGroup> getCollection() {
        return collection;
    }

    public static void updateId(Collection<StudyGroup> collection){
        nextId = collection.stream()
                .filter(Objects::nonNull)
                .map(StudyGroup::getId)
                .max(Integer::compareTo)
                .orElse(0);
        collectionManagerLogger.info("Обновлен айди на " + nextId);
    }

    public static int getNextId(){
        return ++nextId;
    }
    /**
     * Метод скрывающий дату, если она сегодняшняя
     * @param localDateTime объект {@link LocalDateTime}
     * @return вывод даты
     */
    public static String timeFormatter(LocalDateTime localDateTime){
        if (localDateTime == null) return null;
        if (localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .equals(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))){
            return localDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        }
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Метод скрывающий дату, если она сегодняшняя
     * @param dateToConvert объект {@link Date}
     * @return вывод даты
     */
    public static String timeFormatter(Date dateToConvert){
        LocalDateTime localDateTime = dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        if (localDateTime == null) return null;
        if (localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .equals(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))){
            return localDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        }
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getLastInitTime() {
        return timeFormatter(lastInitTime);
    }

    public String getLastSaveTime() {
        return timeFormatter(lastSaveTime);
    }
    /**
     * @return Имя типа коллекции.
     */
    public String collectionType() {
        return collection.getClass().getName();
    }

    public int collectionSize() {
        return collection.size();
    }

    public void clear(){
        this.collection.clear();
        lastInitTime = LocalDateTime.now();
        collectionManagerLogger.info("Коллекция очищена");
    }

    public StudyGroup getLast() {
        return collection.getLast();
    }

    /**
     * @param id ID элемента.
     * @return Элемент по его ID или null, если не найдено.
     */
    public StudyGroup getById(int id) {
        for (StudyGroup element : collection) {
            if (element.getId() == id) return element;
        }
        return null;
    }

    /**
     * Изменить элемент коллекции с таким id
     * @param id id
     * @param newElement новый элемент
     * @throws InvalidForm Нет элемента с таким id
     */
    public void editById(int id, StudyGroup newElement){
        StudyGroup pastElement = this.getById(id);
        this.removeElement(pastElement);
        newElement.setId(id);
        this.addElement(newElement);
        collectionManagerLogger.info("Объект с айди " + id + " изменен", newElement);
    }

    /**
     * @param id ID элемента.
     * @return Проверяет, существует ли элемент с таким ID.
     */
    public boolean checkExist(int id) {
        return collection.stream()
                .anyMatch((x) -> x.getId() == id);
    }

    public void addElement(StudyGroup studyGroup){
        this.lastSaveTime = LocalDateTime.now();
        collection.add(studyGroup);
        collectionManagerLogger.info("Добавлен объект в коллекцию", studyGroup);
    }

    public void addElements(Collection<StudyGroup> collection) throws InvalidForm{
        if (collection == null) return;
        for (StudyGroup studyGroup:collection){
            this.addElement(studyGroup);
        }
    }

    public void removeElement(StudyGroup studyGroup){
        collection.remove(studyGroup);
    }

    public void removeElements(Collection<StudyGroup> collection){this.collection.removeAll(collection);}

    @Override
    public String toString() {
        if (collection.isEmpty()) return "Коллекция пуста!";
        var last = getLast();

        StringBuilder info = new StringBuilder();
        for (StudyGroup studyGroup : collection) {
            info.append(studyGroup);
            if (studyGroup != last) info.append("\n\n");
        }
        return info.toString();
    }
}

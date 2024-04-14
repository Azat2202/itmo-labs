package managers;

import exceptions.InvalidForm;
import models.StudyGroup;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Date;

/**
 * Класс организующий работу с коллекцией
 */
public class CollectionManager {
    private final ArrayDeque<StudyGroup> collection = new ArrayDeque<>();
    /**
     * Дата создания коллекции
     */
    private LocalDateTime lastInitTime;
    /**
     * Дата последнего изменения коллекции
     */
    private LocalDateTime lastSaveTime;

    public CollectionManager() {
        this.lastInitTime = LocalDateTime.now();
        this.lastSaveTime = null;
    }

    public ArrayDeque<StudyGroup> getCollection() {
        return collection;
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
    public void editById(int id, StudyGroup newElement) throws InvalidForm{
        StudyGroup pastElement = this.getById(id);
        this.removeElement(pastElement);
        newElement.setId(id);
        this.addElement(newElement);
        StudyGroup.updateId(this.getCollection());
    }

    /**
     * @param id ID элемента.
     * @return Проверяет, существует ли элемент с таким ID.
     */
    public boolean checkExist(int id) {
        return collection.stream()
                .anyMatch((x) -> x.getId() == id);
    }

    public void addElement(StudyGroup studyGroup) throws InvalidForm{
        this.lastSaveTime = LocalDateTime.now();
        if (!studyGroup.validate()) throw new InvalidForm();
        collection.add(studyGroup);
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

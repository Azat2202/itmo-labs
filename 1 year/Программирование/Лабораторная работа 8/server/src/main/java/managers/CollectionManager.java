package managers;

import exceptions.InvalidForm;
import models.StudyGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.DatabaseHandler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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

    private ReadWriteLock lock = new ReentrantReadWriteLock();
    Lock writeLock = lock.writeLock();
    Lock readLock = lock.readLock();

    private static final Logger collectionManagerLogger = LogManager.getLogger(CollectionManager.class);

    public CollectionManager() {
        this.lastInitTime = LocalDateTime.now();
        this.lastSaveTime = null;
        collection.addAll(DatabaseHandler.getDatabaseManager().loadCollection());
    }

    public ArrayDeque<StudyGroup> getCollection() {
        try {
            readLock.lock();
            return collection;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Метод скрывающий дату, если она сегодняшняя
     *
     * @param localDateTime объект {@link LocalDateTime}
     * @return вывод даты
     */
    public static String timeFormatter(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        if (localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .equals(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))) {
            return localDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        }
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getLastInitTime() {
        try {
            readLock.lock();
            return timeFormatter(lastInitTime);
        } finally {
            readLock.unlock();
        }
    }

    public String getLastSaveTime() {
        try {
            readLock.lock();
            return timeFormatter(lastSaveTime);
        } finally {
            readLock.unlock();
        }

    }

    /**
     * @return Имя типа коллекции.
     */
    public String collectionType() {
        try {
            readLock.lock();
            return collection.getClass().getName();
        } finally {
            readLock.unlock();
        }
    }

    public int collectionSize() {
        try {
            readLock.lock();
            return collection.size();
        } finally {
            readLock.unlock();
        }
    }

    public void clear() {
        try {
            writeLock.lock();
            this.collection.clear();
            lastInitTime = LocalDateTime.now();
            collectionManagerLogger.info("Коллекция очищена");
        } finally {
            writeLock.unlock();
        }
    }

    public StudyGroup getLast() {
        try {
            readLock.lock();
            return collection.getLast();
        } finally {
            readLock.unlock();
        }
    }

    /**
     * @param id ID элемента.
     * @return Элемент по его ID или null, если не найдено.
     */
    public StudyGroup getById(int id) {
        try {
            readLock.lock();
            for (StudyGroup element : collection) {
                if (element.getId() == id) return element;
            }
            return null;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Изменить элемент коллекции с таким id
     *
     * @param id         id
     * @param newElement новый элемент
     * @throws InvalidForm Нет элемента с таким id
     */
    public void editById(int id, StudyGroup newElement) {
        try {
            writeLock.lock();
            StudyGroup pastElement = this.getById(id);
            this.removeElement(pastElement);
            newElement.setId(id);
            this.addElement(newElement);
            collectionManagerLogger.info("Объект с айди " + id + " изменен", newElement);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * @param id ID элемента.
     * @return Проверяет, существует ли элемент с таким ID.
     */
    public boolean checkExist(int id) {
        try {
            readLock.lock();
            return collection.stream()
                    .anyMatch((x) -> x.getId() == id);
        } finally {
            readLock.unlock();
        }
    }

    public void addElement(StudyGroup studyGroup) {
        try {
            writeLock.lock();
            this.lastSaveTime = LocalDateTime.now();
            collection.add(studyGroup);
            collectionManagerLogger.info("Добавлен объект в коллекцию", studyGroup);
        } finally {
            writeLock.unlock();
        }
    }

    public void removeElement(StudyGroup studyGroup) {
        try {
            writeLock.lock();
            collection.remove(studyGroup);
        } finally {
            writeLock.unlock();
        }
    }

    public void removeElements(Collection<StudyGroup> collection) {
        try {
            writeLock.lock();
            this.collection.removeAll(collection);
        } finally {
            writeLock.unlock();
        }
    }

    public void removeElements(List<Integer> deletedIds) {
        try {
            writeLock.lock();
            deletedIds
                    .forEach((id) -> this.collection.remove(this.getById(id)));
        } finally {
            writeLock.unlock();
        }
    }

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

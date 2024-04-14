package commands;

import dtp.Request;
import dtp.Response;
import dtp.ResponseStatus;
import exceptions.IllegalArguments;
import managers.CollectionManager;
import models.StudyGroup;

import java.util.Objects;

/**
 * Команда 'add_if_max'
 * Добавляет элемент в коллекцию если он больше максмального
 */
public class AddIfMax extends Command implements CollectionEditor{
    private final CollectionManager collectionManager;

    public AddIfMax(CollectionManager collectionManager) {
        super("add_if_max", " {element}: добавить элемент в коллекцию если он больше максмального");
        this.collectionManager = collectionManager;
    }

    /**
     * Исполнить команду
     * @param args аргументы команды
     * @throws IllegalArguments неверные аргументы команды
     */
    @Override
    public Response execute(Request request) throws IllegalArguments {
        if (!request.getArgs().isBlank()) throw new IllegalArguments();
        if (Objects.isNull(request.getObject())){
            return new Response(ResponseStatus.ASK_OBJECT, "Для команды " + this.getName() + " требуется объект");
        }
        if (request.getObject().compareTo(collectionManager.getCollection().stream()
                .filter(Objects::nonNull)
                .max(StudyGroup::compareTo)
                .orElse(null)) >= 1)
        {
            collectionManager.addElement(request.getObject());
            return new Response(ResponseStatus.OK,"Объект успешно добавлен");
        }
        return new Response(ResponseStatus.ERROR,"Элемент меньше максимального");
    }
}

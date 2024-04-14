package commands;

import dtp.Request;
import dtp.Response;
import dtp.ResponseStatus;
import dtp.User;
import exceptions.IllegalArguments;
import managers.CollectionManager;
import models.StudyGroup;
import utility.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Команда 'clear'
 * Очищает коллекцию
 */
public class Clear extends Command implements CollectionEditor{
    private CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager) {
        super("clear", ": очистить коллекцию");
        this.collectionManager = collectionManager;
    }

    /**
     * Исполнить команду
     * @param request аргументы команды
     * @throws IllegalArguments неверные аргументы команды
     */
    @Override
    public Response execute(Request request) throws IllegalArguments {
        if (!request.getArgs().isBlank()) throw new IllegalArguments();
        List<Integer> deletedIds = collectionManager.getCollection().stream()
                .filter(studyGroup -> studyGroup.getUserLogin().equals(request.getUser().name()))
                .map(StudyGroup::getId)
                .toList();
        if(DatabaseHandler.getDatabaseManager().deleteAllObjects(request.getUser(), deletedIds)) {
            collectionManager.removeElements(deletedIds);
            return new Response(ResponseStatus.OK, "Ваши элементы удалены");
        }
        return new Response(ResponseStatus.ERROR, "Элементы коллекции удалить не удалось");
    }
}

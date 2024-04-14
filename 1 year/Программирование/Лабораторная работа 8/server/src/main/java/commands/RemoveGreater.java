package commands;

import dtp.Request;
import dtp.Response;
import dtp.ResponseStatus;
import exceptions.ExceptionInFileMode;
import exceptions.IllegalArguments;
import managers.CollectionManager;
import models.StudyGroup;
import utility.DatabaseHandler;

import java.util.Collection;
import java.util.Objects;

/**
 * Команда 'remove_greater'
 * Удаляет из коллекции все элементы, превышающие заданный
 */
public class RemoveGreater extends Command implements CollectionEditor{
    private final CollectionManager collectionManager;

    public RemoveGreater(CollectionManager collectionManager) {
        super("remove_greater", " {element} : удалить из коллекции все элементы, превышающие заданный");
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
        class NoElements extends RuntimeException{

        }
        try {
            if (Objects.isNull(request.getObject())){
                return new Response(ResponseStatus.ASK_OBJECT, "Для команды " + this.getName() + " требуется объект");
            }
            Collection<StudyGroup> toRemove = collectionManager.getCollection().stream()
                    .filter(Objects::nonNull)
                    .filter(studyGroup -> studyGroup.compareTo(request.getObject()) >= 1)
                    .filter(studyGroup -> studyGroup.getUserLogin().equals(request.getUser().name()))
                    .filter((obj) -> DatabaseHandler.getDatabaseManager().deleteObject(obj.getId(), request.getUser()))
                    .toList();
            collectionManager.removeElements(toRemove);
            return new Response(ResponseStatus.OK,"Удалены элементы большие чем заданный");
        } catch (NoElements e){
            return new Response(ResponseStatus.ERROR,"В коллекции нет элементов");
        } catch (ExceptionInFileMode e){
            return new Response(ResponseStatus.ERROR,"Поля в файле не валидны! Объект не создан");
        }
    }
}

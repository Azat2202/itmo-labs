package commands;

import dtp.Request;
import dtp.Response;
import dtp.ResponseStatus;
import exceptions.IllegalArguments;
import managers.CollectionManager;
import models.StudyGroup;

import java.util.Collection;
import java.util.Objects;

/**
 * Команда 'remove_all_by_average_mark'
 * Удаляет из коллекции все элементы, значение поля average_mark которого эквивалентно заданному
 */
public class RemoveAllByAverageMark extends Command implements CollectionEditor{
    private final CollectionManager collectionManager;

    public RemoveAllByAverageMark(CollectionManager collectionManager) {
        super("remove_all_by_average_mark", "  average_mark : удалить из коллекции все элементы, значение поля average_mark которого эквивалентно заданному");
        this.collectionManager = collectionManager;
    }

    /**
     * Исполнить команду
     * @param request аргументы команды
     * @throws IllegalArguments неверные аргументы команды
     */
    @Override
    public Response execute(Request request) throws IllegalArguments {
        if (request.getArgs().isBlank()) throw new IllegalArguments();
        try {
            long averageMark = Long.parseLong(request.getArgs().trim());
            Collection<StudyGroup> toRemove = collectionManager.getCollection().stream()
                    .filter(Objects::nonNull)
                    .filter(studyGroup -> studyGroup.getAverageMark() == averageMark)
                    .toList();
            collectionManager.removeElements(toRemove);
            return new Response(ResponseStatus.OK,"Удалены элементы с таким average_mark");
        } catch (NumberFormatException exception) {
            return new Response(ResponseStatus.ERROR,"average_mark должно быть числом типа long");
        }
    }
}

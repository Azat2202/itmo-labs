package commands;

import dtp.Request;
import dtp.Response;
import dtp.ResponseStatus;
import exceptions.IllegalArguments;
import managers.CollectionManager;
import java.util.Objects;

public class CountByAverageMark extends Command{
    private CollectionManager collectionManager;

    public CountByAverageMark(CollectionManager collectionManager) {
        super("count_by_average_mark", " average_mark : вывести количество элементов, значение поля average_mark которых равно заданному");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) throws IllegalArguments {
        if (request.getArgs().isBlank()) throw new IllegalArguments();
        try {
            long averageMark = Long.parseLong(request.getArgs().trim());
            return new Response(ResponseStatus.OK,"Количество элементов, с меньшим значением поля average_mark: " +
                    collectionManager.getCollection().stream()
                            .filter(Objects::nonNull)
                            .filter(s -> s.getAverageMark() == averageMark)
                            .count());
        } catch (NumberFormatException exception) {
            return new Response(ResponseStatus.ERROR,"average_mark должно быть числом типа long");
        }
    }
}

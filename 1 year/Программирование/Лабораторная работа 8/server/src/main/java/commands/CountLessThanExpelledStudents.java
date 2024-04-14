package commands;

import dtp.Request;
import dtp.Response;
import dtp.ResponseStatus;
import exceptions.IllegalArguments;
import managers.CollectionManager;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Команда 'count_less_than_expelled_students'
 * Выводит количество элементов, значение поля expelled_students которых меньше заданному
 */
public class CountLessThanExpelledStudents extends Command {
    private CollectionManager collectionManager;

    public CountLessThanExpelledStudents(CollectionManager collectionManager) {
        super("count_less_than_expelled_students", " expelled_students : вывести количество элементов, значение поля expelled_students которых меньше заданному");
        this.collectionManager = collectionManager;
    }

    /**
     * Исполнить команду
     *
     * @param args аргументы команды
     * @throws IllegalArguments неверные аргументы команды
     */
    @Override
    public Response execute(Request request) throws IllegalArguments {
        if (request.getArgs().isBlank()) throw new IllegalArguments();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Response", request.getLocale());
        try {
            long expelledStudents = Long.parseLong(request.getArgs().trim());
            return new Response(ResponseStatus.OK, MessageFormat.format(resourceBundle.getString("expelledLess"), collectionManager.getCollection().stream()
                            .filter(Objects::nonNull)
                            .filter(s -> Long.compare(s.getExpelledStudents(), expelledStudents) <= -1)
                            .map(Objects::toString)
                            .count()));

        } catch (NumberFormatException exception) {
            return new Response(ResponseStatus.ERROR, resourceBundle.getString("expelledStudentsLong"));
        }
    }
}

package commandLine.commands;

import commandLine.Console;
import commandLine.ConsoleColors;
import exceptions.IllegalArguments;
import managers.CollectionManager;

import java.util.Objects;

/**
 * Команда 'count_less_than_expelled_students'
 * Выводит количество элементов, значение поля expelled_students которых меньше заданному
 */
public class CountLessThanExpelledStudents extends Command{
    private CollectionManager collectionManager;
    private Console console;

    public CountLessThanExpelledStudents(Console console, CollectionManager collectionManager) {
    super("count_less_than_expelled_students", " expelled_students : вывести количество элементов, значение поля expelled_students которых меньше заданному");
        this.collectionManager = collectionManager;
        this.console = console;
    }

    /**
     * Исполнить команду
     * @param args аргументы команды
     * @throws IllegalArguments неверные аргументы команды
     */
    @Override
    public void execute(String args) throws IllegalArguments {
        if (args.isBlank()) throw new IllegalArguments();
        try {
            long expelledStudents = Long.parseLong(args.trim());
            console.print(ConsoleColors.toColor("Количество элементов, с меньшим значением поля expelled_students: ", ConsoleColors.GREEN));
            console.println(String.valueOf(collectionManager.getCollection().stream()
                    .filter(Objects::nonNull)
                    .filter(s -> Long.compare(s.getExpelledStudents(), expelledStudents) <= -1)
                    .map(Objects::toString)
                    .count()));

        } catch (NumberFormatException exception) {
            console.printError("expelled_students должно быть числом типа long");
        }
    }
}

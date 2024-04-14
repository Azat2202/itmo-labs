package commandLine.commands;

import commandLine.Console;
import commandLine.ConsoleColors;
import exceptions.IllegalArguments;
import managers.CollectionManager;
import models.StudyGroup;

import java.util.Collection;
import java.util.Objects;

/**
 * Команда 'remove_all_by_average_mark'
 * Удаляет из коллекции все элементы, значение поля average_mark которого эквивалентно заданному
 */
public class RemoveAllByAverageMark extends Command{
    private CollectionManager collectionManager;
    private Console console;

    public RemoveAllByAverageMark(Console console, CollectionManager collectionManager) {
        super("remove_all_by_average_mark", "  average_mark : удалить из коллекции все элементы, значение поля average_mark которого эквивалентно заданному");
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
            long averageMark = Long.parseLong(args.trim());
            Collection<StudyGroup> toRemove = collectionManager.getCollection().stream()
                    .filter(Objects::nonNull)
                    .filter(studyGroup -> studyGroup.getAverageMark() == averageMark)
                    .toList();
            collectionManager.removeElements(toRemove);
            console.println(ConsoleColors.toColor("Удалены элементы с таким average_mark", ConsoleColors.GREEN));
        } catch (NumberFormatException exception) {
            console.printError("average_mark должно быть числом типа long");
        }
    }
}

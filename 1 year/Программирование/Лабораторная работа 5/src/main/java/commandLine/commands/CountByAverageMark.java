package commandLine.commands;

import commandLine.Console;
import commandLine.ConsoleColors;
import exceptions.IllegalArguments;
import exceptions.InvalidForm;
import managers.CollectionManager;
import models.StudyGroup;
import models.forms.StudyGroupForm;

import java.util.Collection;
import java.util.Objects;

public class CountByAverageMark extends Command{
    private CollectionManager collectionManager;
    private Console console;

    public CountByAverageMark(Console console, CollectionManager collectionManager) {
        super("count_by_average_mark", " average_mark : вывести количество элементов, значение поля average_mark которых равно заданному");
        this.collectionManager = collectionManager;
        this.console = console;
    }

    @Override
    public void execute(String args) throws IllegalArguments {
        if (args.isBlank()) throw new IllegalArguments();
        try {
            long averageMark = Long.parseLong(args.trim());
            console.print(ConsoleColors.toColor("Количество элементов, с меньшим значением поля average_mark: ", ConsoleColors.GREEN));
            console.println(String.valueOf(collectionManager.getCollection().stream()
                    .filter(Objects::nonNull)
                    .filter(s -> s.getAverageMark() == averageMark)
                    .map(Objects::toString)
                    .count()));

        } catch (NumberFormatException exception) {
            console.printError("average_mark должно быть числом типа long");
        }
    }
}

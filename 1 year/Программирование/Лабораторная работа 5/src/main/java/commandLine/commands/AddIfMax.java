package commandLine.commands;

import com.thoughtworks.xstream.mapper.Mapper;
import commandLine.Console;
import commandLine.ConsoleColors;
import exceptions.ExceptionInFileMode;
import exceptions.IllegalArguments;
import exceptions.InvalidForm;
import managers.CollectionManager;
import models.StudyGroup;
import models.forms.StudyGroupForm;

import java.util.Objects;

/**
 * Команда 'add_if_max'
 * Добавляет элемент в коллекцию если он больше максмального
 */
public class AddIfMax extends Command{
    private CollectionManager collectionManager;
    private Console console;

    public AddIfMax(Console console, CollectionManager collectionManager) {
        super("add_if_max", " {element}: добавить элемент в коллекцию если он больше максмального");
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
        if (!args.isBlank()) throw new IllegalArguments();
        try {
            console.println(ConsoleColors.toColor("Создание объекта StudyGroup", ConsoleColors.PURPLE));
            StudyGroup newElement = new StudyGroupForm(console).build();
            console.println(ConsoleColors.toColor("Создание объекта StudyGroup окончено успешно!", ConsoleColors.PURPLE));
            if (newElement.compareTo(collectionManager.getCollection().stream()
                    .filter(Objects::nonNull)
                    .max(StudyGroup::compareTo)
                    .orElse(null)) >= 1)
            {
                collectionManager.addElement(newElement);
                console.println(ConsoleColors.toColor("Объект успешно добавлен", ConsoleColors.GREEN));
            } else {
                console.println(ConsoleColors.toColor("Элемент меньше максимального", ConsoleColors.RED));
            }
        } catch (InvalidForm invalidForm) {
            console.printError("Поля объекта не валидны! Объект не создан!");
        }  catch (ExceptionInFileMode e){
            console.printError("Поля в файле не валидны! Объект не создан");
        }
    }
}

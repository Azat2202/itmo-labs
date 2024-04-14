package commandLine.commands;

import exceptions.ExceptionInFileMode;
import exceptions.IllegalArguments;
import managers.CollectionManager;
import commandLine.Console;
import commandLine.ConsoleColors;
import exceptions.InvalidForm;
import models.forms.StudyGroupForm;

/**
 * Команда 'add'
 * Добавляет новый элемент в коллекцию
 */
public class AddElement extends Command{
    private CollectionManager collectionManager;
    private Console console;

    public AddElement(Console console, CollectionManager collectionManager) {
        super("add", " {element}: добавить новый элемент в коллекцию");
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
            collectionManager.addElement(new StudyGroupForm(console).build());
            console.println(ConsoleColors.toColor("Создание объекта StudyGroup окончено успешно!", ConsoleColors.PURPLE));
        } catch (InvalidForm invalidForm) {
            console.printError("Поля объекта не валидны! Объект не создан!");
        } catch (ExceptionInFileMode e){
            console.printError("Поля в файле не валидны! Объект не создан");
        }
    }
}

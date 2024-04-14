package commandLine.commands;

import exceptions.IllegalArguments;
import managers.CollectionManager;
import commandLine.*;

/**
 * Команда 'info'
 * Выводит в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
 */
public class Info extends Command{
    private CollectionManager collectionManager;
    private Console console;

    public Info(Console console, CollectionManager collectionManager) {
        super("info", ": вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
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
        String lastInitTime = (collectionManager.getLastInitTime() == null)
                ? "В сессии коллекция не инициализирована"
                : collectionManager.getLastInitTime().toString();
        String lastSaveTime = (collectionManager.getLastSaveTime() == null)
                ? "В сессии коллекция не инициализирована "
                : collectionManager.getLastSaveTime().toString();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Сведения о коллекции: \n")
                .append(ConsoleColors.toColor("Тип: ", ConsoleColors.GREEN) + collectionManager.collectionType() + "\n")
                .append(ConsoleColors.toColor("Количество элементов: ", ConsoleColors.GREEN) + collectionManager.collectionSize() + "\n")
                .append(ConsoleColors.toColor("Дата последней инициализации: ", ConsoleColors.GREEN) + lastInitTime + "\n")
                .append(ConsoleColors.toColor("Дата последнего изменения: ", ConsoleColors.GREEN) + lastSaveTime + "\n");
        console.print(stringBuilder.toString());
    }
}

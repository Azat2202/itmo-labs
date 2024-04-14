package commandLine.commands;

import commandLine.Console;
import commandLine.ConsoleColors;
import exceptions.IllegalArguments;
import managers.CommandManager;

import java.util.List;


/**
 * Команда 'history'
 * Выводит последние 5 команд (без их аргументов)
 */
public class History extends Command{
    private CommandManager commandManager;
    private Console console;

    public History(Console console, CommandManager commandManager) {
        super("history", " вывести последние 5 команд (без их аргументов)");
        this.commandManager = commandManager;
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
        List<String> history= commandManager.getCommandHistory();
        for (String command:history.subList(Math.max(history.size() - 5, 0), history.size())){
            console.println(ConsoleColors.toColor(command, ConsoleColors.CYAN));
        }
    }
}

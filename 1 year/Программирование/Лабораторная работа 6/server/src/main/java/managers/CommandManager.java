package managers;

import commands.CollectionEditor;
import commands.Command;
import dtp.Request;
import dtp.Response;
import exceptions.CommandRuntimeError;
import exceptions.ExitObliged;
import exceptions.IllegalArguments;
import exceptions.NoSuchCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Командный менеджер.
 * Реализует паттерн программирования Command
 */
public class CommandManager{
    /**
     * Поле для хранения комманд в виде Имя-Комманда
     */
    private final HashMap<String, Command> commands = new HashMap<>();
    /**
     * Поле для истории команд
     */
    private final List<String> commandHistory = new ArrayList<>();
    private final FileManager fileManager;

    static final Logger commandManagerLogger = LogManager.getLogger(CommandManager.class);

    public CommandManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void addCommand(Command command){
        this.commands.put(command.getName(), command);
        commandManagerLogger.info("Добавлена команда", command);
    }
    public void addCommand(Collection<Command> commands){
        this.commands.putAll(commands.stream()
                .collect(Collectors.toMap(Command::getName, s -> s)));
        commandManagerLogger.info("Добавлены комманды", commands);
    }
    public Collection<Command> getCommands(){
        return commands.values();
    }

    public void addToHistory(String line){
        if(line.isBlank()) return;
        this.commandHistory.add(line);
        commandManagerLogger.info("Добавлена команда в историю" + line, line);
    }

    public List<String> getCommandHistory(){return commandHistory;}

    /**
     * Выполняет команду
     * @param request - запрос клиента
     * @throws NoSuchCommand такая команда отсутствует
     * @throws IllegalArguments неверные аргументы команды
     * @throws CommandRuntimeError команда выдала ошибку при исполнении
     * @throws ExitObliged команда вызвала выход из программы
     */
    public Response execute(Request request) throws NoSuchCommand, IllegalArguments, CommandRuntimeError, ExitObliged {
        Command command = commands.get(request.getCommandName());
        if (command == null) {
            commandManagerLogger.fatal("Нет такой команды" + request.getCommandName());
            throw new NoSuchCommand();
        }
        Response response = command.execute(request);
        commandManagerLogger.info("Выполнение команды", response);
        if (command instanceof CollectionEditor) {
            commandManagerLogger.info("Файл обновлен");
            fileManager.saveObjects();
        }
        return response;
    }
}

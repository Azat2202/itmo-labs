package commands;

import dtp.Request;
import dtp.Response;
import dtp.ResponseStatus;
import exceptions.ExitObliged;
import exceptions.IllegalArguments;

/**
 * Команда 'exit'
 * завершить программу (без сохранения в файл)
 */
public class Exit extends Command {
    public Exit(){
        super("exit", ": завершить программу (без сохранения в файл)");
    }

    /**
     * Исполнить команду
     * @param args аргументы команды
     * @throws ExitObliged нужен выход из программы
     */
    @Override
    public Response execute(Request request) throws IllegalArguments {
        if (!request.getArgs().isBlank()) throw new IllegalArguments();
        return new Response(ResponseStatus.EXIT);
    }
}

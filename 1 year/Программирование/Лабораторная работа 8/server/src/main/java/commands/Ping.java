package commands;

import dtp.*;
import exceptions.IllegalArguments;
import managers.CommandManager;

/**
 * Команда 'ping'
 * пингануть сервер
 */
public class Ping extends Command {
    public Ping() {
        super("ping", ": пингануть сервер");
    }

    /**
     * Исполнить команду
     * @param request запрос клиента
     * @throws IllegalArguments неверные аргументы команды
     */
    @Override
    public Response execute(Request request) throws IllegalArguments {
        return new Response(ResponseStatus.OK, "pong");
    }
}

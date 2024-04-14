package commands;

import dtp.Request;
import dtp.Response;
import dtp.ResponseStatus;
import exceptions.IllegalArguments;

/**
 * Команда 'execute'
 * выполнить скрипт
 */
public class ExecuteScript extends Command {

    public ExecuteScript() {
        super("execute_script", ": выполнить скрипт");
    }

    /**
     * Исполнить команду
     * @param request запрос клиента
     * @throws IllegalArguments неверные аргументы команды
     */
    @Override
    public Response execute(Request request) throws IllegalArguments {
        if (request.getArgs().isBlank()) throw new IllegalArguments();
        return new Response(ResponseStatus.EXECUTE_SCRIPT, request.getArgs());
    }
}

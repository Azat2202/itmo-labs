package commands;

import dtp.Request;
import dtp.Response;
import dtp.ResponseStatus;
import exceptions.IllegalArguments;

/**
 * Команда 'sleep'
 * ждать определенное количество секунд
 */
public class Sleep extends Command {
    public Sleep() {
        super("sleep", ": ждать определенное количество секунд");
    }

    /**
     * Исполнить команду
     * @param request запрос клиента
     * @throws IllegalArguments неверные аргументы команды
     */
    @Override
    public Response execute(Request request) throws IllegalArguments {
        if (request.getArgs().isBlank()) throw new IllegalArguments();
        if (request.getArgs().isBlank()) throw new IllegalArguments();
        try {
            Integer seconds = Math.toIntExact(Integer.parseInt(request.getArgs().trim()));
            Thread.sleep(seconds * 1000L);
            return new Response(ResponseStatus.OK,"Вы поспали " + seconds + " секунд");
        } catch (NumberFormatException exception) {
            return new Response(ResponseStatus.WRONG_ARGUMENTS,"id должно быть числом типа int");
        } catch (InterruptedException e) {
            return new Response(ResponseStatus.ERROR, "Операция прервана");
        }
    }
}

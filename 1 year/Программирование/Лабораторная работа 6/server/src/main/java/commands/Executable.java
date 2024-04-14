package commands;

import dtp.Request;
import dtp.Response;
import exceptions.CommandRuntimeError;
import exceptions.ExitObliged;
import exceptions.IllegalArguments;

/**
 * Интерфейс для исполняемых команд
 */
public interface Executable {
    Response execute(Request request) throws CommandRuntimeError, ExitObliged, IllegalArguments;
}

package exceptions;

import java.io.IOException;

/**
 * Класс исключения для неверных аргументов команды
 */
public class IllegalArguments extends IOException {
    public IllegalArguments() {
    }

    public IllegalArguments(String message) {
        super(message);
    }

    public IllegalArguments(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalArguments(Throwable cause) {
        super(cause);
    }
}

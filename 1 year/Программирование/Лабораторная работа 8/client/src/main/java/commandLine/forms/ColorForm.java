package commandLine.forms;

import commandLine.*;
import exceptions.ExceptionInFileMode;
import models.Color;
import utility.ConsoleColors;
import utility.ExecuteFileManager;

import java.util.Locale;

/**
 * Форма для выбора цвета
 */
public class ColorForm extends Form<Color>{
    private final Printable console;
    private final UserInput scanner;
    private final String type;

    public ColorForm(Printable console, String type) {
        this.console = (Console.isFileMode())
                ? new BlankConsole()
                : console;
        this.type = type;
        this.scanner = (Console.isFileMode())
                ? new ExecuteFileManager()
                : new ConsoleInput();
    }
    /**
     * Сконструировать новый элемент класса {@link Color}
     * @return объект класса {@link Color}
     */
    @Override
    public Color build() {
        console.println("Возможные цвета: ");
        console.println(Color.names());
        while (true){
            console.println(ConsoleColors.toColor("Введите цвет " + type + ": ", ConsoleColors.GREEN));
            String input = scanner.nextLine().trim();
            try{
                return Color.valueOf(input.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException exception){
                console.printError("Такого цвета нет в списке");
                if (Console.isFileMode()) throw new ExceptionInFileMode();
            }
        }
    }
}

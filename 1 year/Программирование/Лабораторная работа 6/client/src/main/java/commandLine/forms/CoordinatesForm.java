package commandLine.forms;

import exceptions.ExceptionInFileMode;
import commandLine.*;
import models.Coordinates;
import utilty.ConsoleColors;
import utilty.ExecuteFileManager;

/**
 * Форма для координат
 */
public class CoordinatesForm extends Form<Coordinates>{
    private final Printable console;
    private final UserInput scanner;

    public CoordinatesForm(Printable console) {
        this.console = (Console.isFileMode())
                ? new BlankConsole()
                : console;
        this.scanner = (Console.isFileMode())
                ? new ExecuteFileManager()
                : new ConsoleInput();
    }

    /**
     * Сконструировать новый элемент класса {@link Coordinates}
     * @return объект класса {@link Coordinates}
     */
    @Override
    public Coordinates build(){
        return new Coordinates(askX(), askY());
    }

    private Float askX(){
        while (true) {
            console.println(ConsoleColors.toColor("Введите координату X", ConsoleColors.GREEN));
            String input = scanner.nextLine().trim();
            try {
                return Float.parseFloat(input);
            } catch (NumberFormatException exception) {
                console.printError("X должно быть числом типа float");
                if (Console.isFileMode()) throw new ExceptionInFileMode();
            }
        }
    }
    private Double askY(){
        while (true) {
            console.println(ConsoleColors.toColor("Введите координату Y", ConsoleColors.GREEN));
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException exception) {
                console.printError("Y должно быть числом типа double");
                if (Console.isFileMode()) throw new ExceptionInFileMode();
            }
        }
    }
}

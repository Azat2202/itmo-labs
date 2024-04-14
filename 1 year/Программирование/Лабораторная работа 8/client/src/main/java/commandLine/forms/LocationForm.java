package commandLine.forms;

import exceptions.ExceptionInFileMode;
import commandLine.*;
import models.Location;
import utility.ConsoleColors;
import utility.ExecuteFileManager;

/**
 * Форма для локации
 */
public class LocationForm extends Form<Location>{
    private final Printable console;
    private final UserInput scanner;

    public LocationForm(Printable console) {
        this.console = (Console.isFileMode())
                ? new BlankConsole()
                : console;
        this.scanner = (Console.isFileMode())
                ? new ExecuteFileManager()
                : new ConsoleInput();
    }

    /**
     * Сконструировать новый элемент класса {@link Location}
     * @return объект класса {@link Location}
     */
    @Override
    public Location build(){
        return new Location(
                askX(),
                askY(),
                askName());
    }

    private double askX(){
        while (true) {
            console.println(ConsoleColors.toColor("Введите координату X", ConsoleColors.GREEN));
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException exception) {
                console.printError("X должно быть числом типа double");
                if (Console.isFileMode()) throw new ExceptionInFileMode();
            }
        }
    }

    private long askY(){
        while (true) {
            console.println(ConsoleColors.toColor("Введите координату Y", ConsoleColors.GREEN));
            String input = scanner.nextLine().trim();
            try {
                return Long.parseLong(input);
            } catch (NumberFormatException exception) {
                console.printError("Y должно быть числом типа long");
                if (Console.isFileMode()) throw new ExceptionInFileMode();
            }
        }
    }

    private String askName(){
        while (true){
            console.println(ConsoleColors.toColor("Введите название локации", ConsoleColors.GREEN));
            String name = scanner.nextLine().trim();
            if (name.isEmpty()){
                console.printError("Имя не может быть пустым");
                if (Console.isFileMode()) throw new ExceptionInFileMode();
            }
            else{
                return name;
            }
        }
    }
}

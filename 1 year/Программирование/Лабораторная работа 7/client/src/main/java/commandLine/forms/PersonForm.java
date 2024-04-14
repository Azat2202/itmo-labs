package commandLine.forms;

import exceptions.ExceptionInFileMode;
import commandLine.*;
import models.*;
import utility.ConsoleColors;
import utility.ExecuteFileManager;

/**
 * Форма для создания человека
 */
public class PersonForm extends Form<Person>{

    private final Printable console;
    private final UserInput scanner;

    public PersonForm(Printable console) {
        this.console = (Console.isFileMode())
                ? new BlankConsole()
                : console;
        this.scanner = (Console.isFileMode())
                ? new ExecuteFileManager()
                : new ConsoleInput();
    }

    /**
     * Сконструировать новый элемент класса {@link Person}
     * @return объект класса {@link Person}
     */
    @Override
    public Person build() {
        console.println(ConsoleColors.toColor("Создание объекта админа", ConsoleColors.PURPLE));
        Person person = new Person(
                askName(),
                askWeight(),
                askEyeColor(),
                askHairColor(),
                askNationality(),
                askLocation()
        );
        console.println(ConsoleColors.toColor("Создание объекта админа окончено успешно", ConsoleColors.PURPLE));
        return person;
    }

    private String askName(){
        String name;
        while (true){
            console.println(ConsoleColors.toColor("Введите имя", ConsoleColors.GREEN));
            name = scanner.nextLine().trim();
            if (name.isEmpty()){
                console.printError("Имя не может быть пустым");
                if (Console.isFileMode()) throw new ExceptionInFileMode();
            }
            else{
                return name;
            }
        }
    }

    private int askWeight(){
        while (true) {
            console.println(ConsoleColors.toColor("Введите количество студентов", ConsoleColors.GREEN));
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                console.printError("Число студентов должно быть числом типа long");
                if (Console.isFileMode()) throw new ExceptionInFileMode();
            }
        }
    }

    private Color askEyeColor(){
        return new ColorForm(console, "глаз").build();
    }
    private Color askHairColor(){
        return new ColorForm(console, "волсо").build();
    }

    private Country askNationality(){
        return new NationalityForm(console).build();
    }
    private Location askLocation(){
        return new LocationForm(console).build();
    }
}

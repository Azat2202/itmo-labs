package commandLine.forms;

import exceptions.ExceptionInFileMode;
import commandLine.*;
import models.Coordinates;
import models.FormOfEducation;
import models.Person;
import models.StudyGroup;
import utility.ConsoleColors;
import utility.ExecuteFileManager;

import java.util.Date;

/**
 * Форма учебной группы
 * @author azat2202
 */
public class StudyGroupForm extends Form<StudyGroup>{
    private final Printable console;
    private final UserInput scanner;

    public StudyGroupForm(Printable console) {
        this.console = (Console.isFileMode())
            ? new BlankConsole()
            : console;
        this.scanner = (Console.isFileMode())
                ? new ExecuteFileManager()
                : new ConsoleInput();
    }

    /**
     * Сконструировать новый элемент класса {@link StudyGroup}
     * @return объект класса {@link StudyGroup}
     */
    @Override
    public StudyGroup build(){
        return new StudyGroup(
                askName(),
                askCoordinates(),
                new Date(),
                askStudentsCount(),
                askExpelledStudents(),
                askAverageMark(),
                askFormOfEducation(),
                askGroupAdmin()
        );
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

    private Coordinates askCoordinates(){
        return new CoordinatesForm(console).build();
    }

    private Long askStudentsCount(){
        while (true) {
            console.println(ConsoleColors.toColor("Введите количество студентов", ConsoleColors.GREEN));
            String input = scanner.nextLine().trim();
            try {
                return Long.parseLong(input);
            } catch (NumberFormatException exception) {
                console.printError("Число студентов должно быть числом типа long");
                if (Console.isFileMode()) throw new ExceptionInFileMode();
            }
        }
    }

    private long askExpelledStudents(){
        while (true) {
            console.println(ConsoleColors.toColor("Введите количество отчисленных студентов", ConsoleColors.GREEN));
            String input = scanner.nextLine().trim();
            try {
                return Long.parseLong(input);
            } catch (NumberFormatException exception) {
                console.printError("Число студентов должно быть числом типа long");
                if (Console.isFileMode()) throw new ExceptionInFileMode();
            }
        }
    }

    private long askAverageMark(){
        while (true) {
            console.println(ConsoleColors.toColor("Введите среднюю оценку", ConsoleColors.GREEN));
            String input = scanner.nextLine().trim();
            try {
                return Long.parseLong(input);
            } catch (NumberFormatException exception) {
                console.printError("Оценка должна быть числом типа long");
                if (Console.isFileMode()) throw new ExceptionInFileMode();
            }
        }
    }

    private FormOfEducation askFormOfEducation(){
        return new FormOfEducationForm(console).build();
    }

    private Person askGroupAdmin(){
        return new PersonForm(console).build();
    }
}

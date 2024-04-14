package commandLine.forms;

import commandLine.*;
import exceptions.ExceptionInFileMode;
import models.FormOfEducation;
import utilty.ConsoleColors;
import utilty.ExecuteFileManager;

import java.util.Locale;

/**
 * Форма для формы обучения
 */
public class FormOfEducationForm extends Form<FormOfEducation>{
    private final Printable console;
    private final UserInput scanner;

    public FormOfEducationForm(Printable console) {
        this.console = (Console.isFileMode())
                ? new BlankConsole()
                : console;
        this.scanner = (Console.isFileMode())
                ? new ExecuteFileManager()
                : new ConsoleInput();
    }

    /**
     * Сконструировать новый элемент Enum {@link FormOfEducation}
     * @return объект Enum {@link FormOfEducation}
     */
    @Override
    public FormOfEducation build() {
        console.println("Возможные формы обучения: ");
        console.println(FormOfEducation.names());
        while (true){
            console.println(ConsoleColors.toColor("Введите форму обучения: ", ConsoleColors.GREEN));
            String input = scanner.nextLine().trim();
            try{
                return FormOfEducation.valueOf(input.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException exception){
                console.printError("Такой формы обучения нет в списке");
                if (Console.isFileMode()) throw new ExceptionInFileMode();
            }
        }
    }
}

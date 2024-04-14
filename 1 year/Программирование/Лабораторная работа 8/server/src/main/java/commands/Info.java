package commands;

import dtp.Request;
import dtp.Response;
import dtp.ResponseStatus;
import exceptions.IllegalArguments;
import managers.CollectionManager;
import utility.ConsoleColors;

import java.util.ResourceBundle;

/**
 * Команда 'info'
 * Выводит в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
 */
public class Info extends Command{
    private CollectionManager collectionManager;

    public Info(CollectionManager collectionManager) {
        super("info", ": вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
        this.collectionManager = collectionManager;
    }

    /**
     * Исполнить команду
     * @param request аргументы команды
     * @throws IllegalArguments неверные аргументы команды
     */
    @Override
    public Response execute(Request request) throws IllegalArguments {
        if (!request.getArgs().isBlank()) throw new IllegalArguments();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Response", request.getLocale());
        String lastInitTime = (collectionManager.getLastInitTime() == null)
                ? resourceBundle.getString("noCollectionInSession")
                : collectionManager.getLastInitTime().toString();
        String lastSaveTime = (collectionManager.getLastSaveTime() == null)
                ? resourceBundle.getString("noCollectionInSession")
                : collectionManager.getLastSaveTime().toString();
        String stringBuilder = resourceBundle.getString("CollectionInfo") +
                resourceBundle.getString("type") + collectionManager.collectionType() + "\n" +
                resourceBundle.getString("elementsCount") + collectionManager.collectionSize() + "\n" +
                resourceBundle.getString("lastInitTime") + lastInitTime + "\n";
        return new Response(ResponseStatus.OK, stringBuilder);
    }
}

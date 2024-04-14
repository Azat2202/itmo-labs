package commands;

import dtp.Request;
import dtp.Response;
import dtp.ResponseStatus;
import exceptions.IllegalArguments;
import managers.CollectionManager;
import models.StudyGroup;
import utility.DatabaseHandler;

import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Команда 'add_if_max'
 * Добавляет элемент в коллекцию если он больше максмального
 */
public class AddIfMax extends Command implements CollectionEditor{
    private final CollectionManager collectionManager;

    public AddIfMax(CollectionManager collectionManager) {
        super("add_if_max", " {element}: добавить элемент в коллекцию если он больше максмального");
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
        if (Objects.isNull(request.getObject())){
            return new Response(ResponseStatus.ASK_OBJECT, resourceBundle.getString("objNeed") + getName() + resourceBundle.getString("ForCommandObjecRrequired"));
        }
        if (request.getObject().compareTo(collectionManager.getCollection().stream()
                .filter(Objects::nonNull)
                .max(StudyGroup::compareTo)
                .orElse(null)) >= 1)
        {
            int new_id = DatabaseHandler.getDatabaseManager().addObject(request.getObject(), request.getUser());
            if(new_id == -1) return new Response(ResponseStatus.ERROR, resourceBundle.getString("objNotSucceedAdd"));
            request.getObject().setId(new_id);
            request.getObject().setUserLogin(request.getUser().name());
            collectionManager.addElement(request.getObject());
            return new Response(ResponseStatus.OK, resourceBundle.getString("objAddOl"));
        }
        return new Response(ResponseStatus.ERROR, resourceBundle.getString("elementLess"));
    }
}

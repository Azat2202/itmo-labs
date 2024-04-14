package commands;

import dtp.Request;
import dtp.Response;
import dtp.ResponseStatus;
import exceptions.IllegalArguments;
import managers.CollectionManager;
import utility.DatabaseHandler;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Команда 'add'
 * Добавляет новый элемент в коллекцию
 */
public class AddElement extends Command implements CollectionEditor{
    private final CollectionManager collectionManager;

    public AddElement(CollectionManager collectionManager) {
        super("add", " {element}: добавить новый элемент в коллекцию");
        this.collectionManager = collectionManager;
    }

    /**
     * Исполнить команду
     * @param request запрос клиента
     * @throws IllegalArguments неверные аргументы команды
     */
    @Override
    public Response execute(Request request) throws IllegalArguments {
        if (!request.getArgs().isBlank()) throw new IllegalArguments();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Response", request.getLocale());
        if (Objects.isNull(request.getObject())){
            return new Response(ResponseStatus.ASK_OBJECT, MessageFormat.format(resourceBundle.getString("objNeed") + getName() + resourceBundle.getString("ForCommandObjecRrequired"), this.getName()));
        } else{
            int new_id = DatabaseHandler.getDatabaseManager().addObject(request.getObject(), request.getUser());
            if(new_id == -1) return new Response(ResponseStatus.ERROR, resourceBundle.getString("objNotSucceedAdd"));
            request.getObject().setId(new_id);
            request.getObject().setUserLogin(request.getUser().name());
            collectionManager.addElement(request.getObject());
            return new Response(ResponseStatus.OK, resourceBundle.getString("objAddOl"));
        }
    }
}

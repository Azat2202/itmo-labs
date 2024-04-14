package managers;

import dtp.Request;
import dtp.Response;
import dtp.ResponseStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.RequestHandler;

import java.io.*;
import java.nio.channels.SocketChannel;
import java.util.concurrent.*;

public class ConnectionManager implements Runnable{
    private final CommandManager commandManager;
    private final DatabaseManager databaseManager;
    private static final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(8);
    private static final ForkJoinPool forkJoinPool = new ForkJoinPool();
    private final SocketChannel clientSocket;

    static final Logger connectionManagerLogger = LogManager.getLogger(ConnectionManager.class);

    public ConnectionManager(CommandManager commandManager, SocketChannel clientSocket, DatabaseManager databaseManager) {
        this.commandManager = commandManager;
        this.clientSocket = clientSocket;
        this.databaseManager = databaseManager;
    }

    @Override
    public void run(){
        Request userRequest = null;
        Response responseToUser = null;
        try {
            ObjectInputStream clientReader = new ObjectInputStream(clientSocket.socket().getInputStream());
            ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.socket().getOutputStream());
            while (true){
                userRequest = (Request) clientReader.readObject();
                connectionManagerLogger.info("Получен запрос с командой " + userRequest.getCommandName(), userRequest);
                if(!databaseManager.confirmUser(userRequest.getUser())
                        && !userRequest.getCommandName().equals("register")){
                    connectionManagerLogger.info("Юзер не одобрен");
                    responseToUser = new Response(ResponseStatus.LOGIN_FAILED, "Неверный пользователь!");
                    submitNewResponse(new ConnectionManagerPool(responseToUser, clientWriter));
                } else{
                    FutureManager.addNewFixedThreadPoolFuture(fixedThreadPool.submit(new RequestHandler(commandManager, userRequest, clientWriter)));
                }
            }
        } catch (ClassNotFoundException exception) {
            connectionManagerLogger.fatal("Произошла ошибка при чтении полученных данных!");
        }catch (CancellationException exception) {
            connectionManagerLogger.warn("При обработке запроса произошла ошибка многопоточности!");
        } catch (InvalidClassException | NotSerializableException exception) {
            connectionManagerLogger.error("Произошла ошибка при отправке данных на клиент!");
        } catch (IOException exception) {
            if (userRequest == null) {
                connectionManagerLogger.error("Непредвиденный разрыв соединения с клиентом!");
            } else {
                connectionManagerLogger.info("Клиент успешно отключен от сервера!");
            }
        }
    }

    public static void submitNewResponse(ConnectionManagerPool connectionManagerPool){
        forkJoinPool.submit(() -> {
            try {
                connectionManagerPool.objectOutputStream().writeObject(connectionManagerPool.response());
                connectionManagerPool.objectOutputStream().flush();
            } catch (IOException e) {
                connectionManagerLogger.error("Не удалось отправить ответ");
                connectionManagerLogger.debug(e);
            }
        });
    }
}

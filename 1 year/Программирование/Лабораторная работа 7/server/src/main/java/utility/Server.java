package utility;

import main.App;
import exceptions.ConnectionErrorException;
import exceptions.OpeningServerException;
import managers.CommandManager;
import managers.ConnectionManager;
import managers.DatabaseManager;
import managers.FutureManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    private int port;
    private int soTimeout;
    private Printable console;
    private ServerSocketChannel ss;
    private SocketChannel socketChannel;
    private CommandManager commandManager;

    private static final Logger serverLogger = LogManager.getLogger(Server.class);
    public static final Logger rootLogger = LogManager.getLogger(App.class);

    private final DatabaseManager databaseManager;

    private final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    public Server(CommandManager commandManager, DatabaseManager databaseManager) {
        this.port = App.PORT;
        this.soTimeout = App.CONNECTION_TIMEOUT;
        this.console = new BlankConsole();
        this.commandManager = commandManager;
        this.databaseManager = databaseManager;
    }

    public void run(){
        try{
            openServerSocket();
            rootLogger.info("--------------------------------------------------------------------");
            rootLogger.info("-----------------СЕРВЕР УСПЕШНО ЗАПУЩЕН-----------------------------");
            rootLogger.info("--------------------------------------------------------------------");
            while(true){
                FutureManager.checkAllFutures();
                try{
                    cachedThreadPool.submit(new ConnectionManager(commandManager, connectToClient(), databaseManager));
                } catch (ConnectionErrorException  ignored){}
            }
        } catch (OpeningServerException e) {
            serverLogger.fatal("Сервер не может быть запущен");
        }
        stop();
    }

    private void openServerSocket() throws OpeningServerException{
        try {
            SocketAddress socketAddress = new InetSocketAddress(port);
            ss = ServerSocketChannel.open();
            ss.bind(socketAddress);
        } catch (IllegalArgumentException exception) {
            serverLogger.error("Порт находится за пределами возможных значений");
            throw new OpeningServerException();
        } catch (IOException exception) {
            serverLogger.error("Произошла ошибка при попытке использовать порт " + port);
            throw new OpeningServerException();
        }
    }

    private SocketChannel connectToClient() throws ConnectionErrorException{
        try {
            ss.socket().setSoTimeout(50);
            socketChannel = ss.socket().accept().getChannel();
            serverLogger.info("Соединение с клиентом успешно установлено.");
            return socketChannel;
        } catch (IOException e) {
            throw new ConnectionErrorException();
        }
    }



    private void stop() {
        class ClosingSocketException extends Exception{}
            try{
                if (socketChannel == null) throw new ClosingSocketException();
                socketChannel.close();
                ss.close();
                serverLogger.info("все соединения закрыты");
            } catch (ClosingSocketException exception) {
                serverLogger.fatal("Невозможно завершить работу еще не запущенного сервера!");
            } catch (IOException exception) {
                    serverLogger.fatal("Произошла ошибка при завершении работы сервера!");
            }
    }
}

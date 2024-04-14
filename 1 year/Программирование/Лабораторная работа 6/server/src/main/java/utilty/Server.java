package utilty;

import dtp.Request;
import dtp.Response;
import exceptions.ConnectionErrorException;
import exceptions.OpeningServerException;
import managers.FileManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner;


public class Server {
    private int port;
    private int soTimeout;
    private Printable console;
    private ServerSocketChannel ss;
    private SocketChannel socketChannel;
    private RequestHandler requestHandler;

    static final Logger serverLogger = LogManager.getLogger(Server.class);

    BufferedInputStream bf = new BufferedInputStream(System.in);

    BufferedReader scanner = new BufferedReader(new InputStreamReader(bf));
    private FileManager fileManager;

    public Server(int port, int soTimeout, RequestHandler handler, FileManager fileManager) {
        this.port = port;
        this.soTimeout = soTimeout;
        this.console = new BlankConsole();
        this.requestHandler = handler;
        this.fileManager = fileManager;
    }

    public void run(){
        try{
            openServerSocket();
            serverLogger.info("Создано соединение с клиентом");
            while (true) {
                try {
                    if (scanner.ready()) {
                        String line = scanner.readLine();
                        if (line.equals("save") || line.equals("s")) {
                            fileManager.saveObjects();
                            serverLogger.info("Коллекция сохранена");
                        }
                    }
                } catch (IOException ignored) {}
                try (SocketChannel clientSocket = connectToClient()) {
                    if(clientSocket == null) continue;
                    clientSocket.configureBlocking(false);
                    if(!processClientRequest(clientSocket)) break;
                } catch (ConnectionErrorException | SocketTimeoutException ignored) {
                } catch (IOException exception) {
                    console.printError("Произошла ошибка при попытке завершить соединение с клиентом!");
                    serverLogger.error("Произошла ошибка при попытке завершить соединение с клиентом!");
                }
            }
            stop();
            serverLogger.info("Соединение закрыто");
        } catch (OpeningServerException e) {
            console.printError("Сервер не может быть запущен");
            serverLogger.fatal("Сервер не может быть запущен");
        }
    }

    private void openServerSocket() throws OpeningServerException{
        try {
            SocketAddress socketAddress = new InetSocketAddress(port);
            serverLogger.debug("Создан сокет");
            ss = ServerSocketChannel.open();
            serverLogger.debug("Создан канал");
            ss.bind(socketAddress);
            ss.configureBlocking(false);
            serverLogger.debug("Открыт канал сокет");
        } catch (IllegalArgumentException exception) {
            console.printError("Порт '" + port + "' находится за пределами возможных значений!");
            serverLogger.error("Порт находится за пределами возможных значений");
            throw new OpeningServerException();
        } catch (IOException exception) {
            serverLogger.error("Произошла ошибка при попытке использовать порт");
            console.printError("Произошла ошибка при попытке использовать порт '" + port + "'!");
            throw new OpeningServerException();
        }
    }

    private SocketChannel connectToClient() throws ConnectionErrorException, SocketTimeoutException {
        try {
            socketChannel = ss.accept();
            return socketChannel;
        } catch (SocketTimeoutException exception) {
            throw new SocketTimeoutException();
        } catch (IOException exception) {
            serverLogger.fatal("Произошла ошибка при соединении с клиентом!");
            throw new ConnectionErrorException();
        }
    }

    private boolean processClientRequest(SocketChannel clientSocket) {
        Request userRequest = null;
        Response responseToUser = null;
        try {
            Request request = getSocketObjet(clientSocket);
            serverLogger.info("Получен реквест с командой" + request.getCommandName(), userRequest);
            console.println(request.toString());
            responseToUser = requestHandler.handle(request);
            sendSocketObject(clientSocket, responseToUser);
            serverLogger.info("Отправлен ответ", responseToUser);
        } catch (ClassNotFoundException exception) {
            console.printError("Произошла ошибка при чтении полученных данных!");
            serverLogger.fatal("Произошла ошибка при чтении полученных данных!");
        } catch (InvalidClassException | NotSerializableException exception) {
            console.printError("Произошла ошибка при отправке данных на клиент!");
            serverLogger.error("Произошла ошибка при отправке данных на клиент!");
        } catch (IOException exception) {
            if (userRequest == null) {
                console.printError("Непредвиденный разрыв соединения с клиентом!");
                serverLogger.error("Непредвиденный разрыв соединения с клиентом!");
            } else {
                console.println("Клиент успешно отключен от сервера!");
                serverLogger.info("Клиент успешно отключен от сервера!");
            }
        }
        return true;
    }

    public static Request getSocketObjet(SocketChannel channel) throws IOException, ClassNotFoundException {
        ByteBuffer buffer = ByteBuffer.allocate(1024*10);
        while (true) {
            try {
                channel.read(buffer);
                buffer.mark();
                byte[] buf = buffer.array();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                return (Request) objectInputStream.readObject();
            } catch (StreamCorruptedException e) {
                // if we cannot readObject we havent read yet, so try another time
            }
        }
    }

    private static void sendSocketObject(SocketChannel client, Response response) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(response);
        objectOutputStream.flush();
        client.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
    }

    private void stop() {
        class ClosingSocketException extends Exception{}
            try{
                if (socketChannel == null) throw new ClosingSocketException();
                socketChannel.close();
                ss.close();
                serverLogger.info("все соединения закрыты");
            } catch (ClosingSocketException exception) {
                console.printError("Невозможно завершить работу еще не запущенного сервера!");
                serverLogger.fatal("Невозможно завершить работу еще не запущенного сервера!");
            } catch (IOException exception) {
                    console.printError("Произошла ошибка при завершении работы сервера!");
                    serverLogger.fatal("Произошла ошибка при завершении работы сервера!");
            }
    }
}

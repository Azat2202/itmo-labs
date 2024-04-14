package utility;

import commandLine.Printable;
import dtp.Request;
import dtp.Response;
import dtp.ResponseStatus;
import dtp.User;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class Client {
    private final String host;
    private final int port;
    private final int reconnectionTimeout;
    private int reconnectionAttempts;
    private final int maxReconnectionAttempts;
    private final Printable console;
    private Socket socket;
    private ObjectOutputStream serverWriter;
    private ObjectInputStream serverReader;


    public Client(String host, int port, int reconnectionTimeout, int maxReconnectionAttempts, Printable console) {
        this.host = host;
        this.port = port;
        this.reconnectionTimeout = reconnectionTimeout;
        this.maxReconnectionAttempts = maxReconnectionAttempts;
        this.console = console;
    }

    public Response sendAndAskResponse(Request request){
        while (true) {
            try {
                if(Objects.isNull(serverWriter) || Objects.isNull(serverReader)) throw new IOException();
                if (request.isEmpty()) return new Response(ResponseStatus.WRONG_ARGUMENTS, "Запрос пустой!");
                serverWriter.writeObject(request);
                serverWriter.flush();
                Response response = (Response) serverReader.readObject();
                this.disconnectFromServer();
                reconnectionAttempts = 0;
                return response;
            } catch (IOException e) {
                if (reconnectionAttempts == 0){
                    // console.println("Установка подключения к серверу", ConsoleColors.GREEN);
                    connectToServer();
                    reconnectionAttempts++;
                    continue;
                } else {
                    console.printError("Соединение с сервером разорвано");
                }
                try {
                    reconnectionAttempts++;
                    if (reconnectionAttempts >= maxReconnectionAttempts) {
                        console.printError("Превышено максимальное количество попыток соединения с сервером");
                        return new Response(ResponseStatus.EXIT);
                    }
                    console.println("Повторная попытка через " + reconnectionTimeout / 1000 + " секунд");
                    Thread.sleep(reconnectionTimeout);
                    connectToServer();
                } catch (Exception exception) {
                    console.printError("Попытка соединения с сервером неуспешна");
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void connectToServer(){
        try{
            if(reconnectionAttempts > 0) console.println("Попытка повторного подключения", ConsoleColors.CYAN);
            this.socket = new Socket(host, port);
            this.serverWriter = new ObjectOutputStream(socket.getOutputStream());
            this.serverReader = new ObjectInputStream(socket.getInputStream());
        } catch (IllegalArgumentException e){
            console.printError("Адрес сервера введен некорректно");
        } catch (IOException e) {
            console.printError("Произошла ошибка при соединении с сервером");
        }
    }

    public void disconnectFromServer(){
        try {
            this.socket.close();
            serverWriter.close();
            serverReader.close();
        } catch (IOException e) {
            console.printError("Не подключен к серверу");
        }
    }
}

package utility;

import commandLine.BlankConsole;
import commandLine.Printable;
import dtp.Request;
import dtp.Response;
import dtp.ResponseStatus;
import dtp.User;
import gui.GuiManager;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class Client {
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("GuiLabels", GuiManager.getLocale());
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
        this.console = new BlankConsole();
    }

    public Response sendAndAskResponse(Request request){
        while (true) {
            try {
                if(Objects.isNull(serverWriter) || Objects.isNull(serverReader)) throw new IOException();
                if (request.isEmpty()) return new Response(ResponseStatus.WRONG_ARGUMENTS, resourceBundle.getString("EmptyrRequest"));
                serverWriter.writeObject(request);
                serverWriter.flush();
                Response response = (Response) serverReader.readObject();
                this.disconnectFromServer();
                reconnectionAttempts = 0;
                return response;
            } catch (IOException e) {
                if (reconnectionAttempts == 0){
                    connectToServer();
                    reconnectionAttempts++;
                    continue;
                }
                try {
                    reconnectionAttempts++;
                    if (reconnectionAttempts >= maxReconnectionAttempts) {
                        JOptionPane.showMessageDialog(null,
                                resourceBundle.getString("ToMuchTries"),
                                resourceBundle.getString("ServerNotAvailable"),
                                JOptionPane.ERROR_MESSAGE);
                        System.exit(666);
                    }
                    AtomicInteger seconds = new AtomicInteger(reconnectionTimeout);
                    JOptionPane optionPane = new JOptionPane(resourceBundle.getString("ServerConnectionBreaked"));
                    JDialog dialog = optionPane.createDialog(null, resourceBundle.getString("ServerConnectionBreaked"));
                    dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                    new Timer(1000, (i) -> {
                        seconds.decrementAndGet();
                        if (seconds.get() <= 0) {
                            dialog.dispose();
                        } else {
                            optionPane.setMessage(MessageFormat.format(
                                    resourceBundle.getString("NextTryIn") + seconds, resourceBundle.getString("ServerConnectionBreaked"),seconds));
                        }
                    }).start();
                    dialog.setVisible(true);
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

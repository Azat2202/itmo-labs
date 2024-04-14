package main;

import commandLine.BlankConsole;
import commandLine.Console;
import commandLine.Printable;
import exceptions.IllegalArguments;
import gui.GuiManager;
import utility.Client;

import java.util.Locale;

public class App {
    //-----------------------SETTINGS---------------------------
    public final static int RECONNECTION_TIMEOUT = 5;
    public final static int MAX_RECONNECTION_ATTEMPTS = 5;
    public final static int APP_DEFAULT_WIDTH = 1500;
    public final static int APP_DEFAULT_HEIGHT = 800;
    //----------------------------------------------------------


    private static String host;
    private static int port;
    private static Printable console = new BlankConsole();

    public static boolean parseHostPort(String[] args){
        try{
            if(args.length != 2) throw new IllegalArguments("Передайте хост и порт в аргументы " +
                    "командной строки в формате <host> <port>");
            host = args[0];
            port = Integer.parseInt(args[1]);
            if(port < 0) throw new IllegalArguments("Порт должен быть натуральным числом");
            return true;
        } catch (IllegalArguments e) {
            console.printError(e.getMessage());
        }
        return false;
    }

    public static void main(String[] args) {
        if (!parseHostPort(args)) return;

        console = new Console();
        Client client = new Client(host, port, RECONNECTION_TIMEOUT, MAX_RECONNECTION_ATTEMPTS, console);
        new GuiManager(client);
//        new RuntimeManager(console, new Scanner(System.in), client).interactiveMode();
    }
}

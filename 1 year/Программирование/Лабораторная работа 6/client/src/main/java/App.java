import commandLine.BlankConsole;
import commandLine.Console;
import commandLine.Printable;
import exceptions.IllegalArguments;
import utilty.Client;
import utilty.RuntimeManager;

import java.util.Scanner;

public class App {
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
        Client client = new Client(host, port, 5000, 5, console);
        new RuntimeManager(console, new Scanner(System.in), client).interactiveMode();
    }
}

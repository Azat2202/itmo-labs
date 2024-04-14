package utility;

import commandLine.BlankConsole;
import commandLine.Printable;
import commandLine.UserInput;
import commandLine.forms.StudyGroupForm;
import dtp.Request;
import dtp.Response;
import dtp.ResponseStatus;
import dtp.User;
import exceptions.ExceptionInFileMode;
import exceptions.ExitObliged;
import exceptions.LoginDuringExecuteFail;
import gui.GuiManager;
import models.StudyGroup;

import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Класс для хранения файл менеджера для команды execute
 */
public class ExecuteFileManager implements UserInput {
    private static final ArrayDeque<String> pathQueue = new ArrayDeque<>();
    private static final ArrayDeque<BufferedReader> fileReaders = new ArrayDeque<>();
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("GuiLabels", GuiManager.getLocale());


    private final Printable console;
    private final Client client;
    private User user;

    public ExecuteFileManager() {
        this.console = new BlankConsole();
        this.client = null;
        this.user = null;
    }
    public ExecuteFileManager(Printable console, Client client, User user) {
        this.console = console;
        this.client = client;
        this.user = user;
    }

    public void fileExecution(String args) throws ExitObliged, LoginDuringExecuteFail {
        if (args == null || args.isEmpty()) {
            console.printError(resourceBundle.getString("ErrorFile"));
            return;
        }
        else console.println(ConsoleColors.toColor(resourceBundle.getString("FileGot"), ConsoleColors.PURPLE));
        args = args.trim();
        try {
            ExecuteFileManager.pushFile(args);
            for (String line = ExecuteFileManager.readLine(); line != null; line = ExecuteFileManager.readLine()) {
                String[] userCommand = (line + " ").split(" ", 2);
                if (userCommand[0].isBlank()) return;
                userCommand[0] = userCommand[0].trim();
                userCommand[1] = userCommand[1].trim();
                if (userCommand[0].equals("execute_script")){
                    if(ExecuteFileManager.fileRepeat(userCommand[1])){
                        console.printError(MessageFormat.format(resourceBundle.getString("FoundRecursion"), new File(userCommand[1]).getAbsolutePath()));
                        continue;
                    }
                }
                console.println(ConsoleColors.toColor(resourceBundle.getString("DoingCommand") + userCommand[0], ConsoleColors.YELLOW));
                Response response = client.sendAndAskResponse(new Request(userCommand[0], userCommand[1], user, GuiManager.getLocale()));
                this.printResponse(response);
                switch (response.getStatus()){
                    case ASK_OBJECT -> {
                        StudyGroup studyGroup;
                        try{
                            studyGroup = new StudyGroupForm(console).build();
                            if (!studyGroup.validate()) throw new ExceptionInFileMode();
                        } catch (ExceptionInFileMode err){
                            console.printError(resourceBundle.getString("VariablesNotValid"));
                            continue;
                        }
                        Response newResponse = client.sendAndAskResponse(
                                new Request(
                                        userCommand[0].trim(),
                                        userCommand[1].trim(),
                                        user,
                                        studyGroup,
                                        GuiManager.getLocale()));
                        if (newResponse.getStatus() != ResponseStatus.OK){
                            console.printError(newResponse.getResponse());
                        }
                        else {
                            this.printResponse(newResponse);
                        }
                    }
                    case EXIT -> throw new ExitObliged();
                    case EXECUTE_SCRIPT -> {
                        this.fileExecution(response.getResponse());
                    }
                    case LOGIN_FAILED -> {
                        console.printError("Ошибка с вашим аккаунтом. Зайдите в него снова");
                        this.user = null;
                        throw new LoginDuringExecuteFail();
                    }
                    default -> {}
                }
            }
            ExecuteFileManager.popFile();
        } catch (FileNotFoundException fileNotFoundException){
            console.printError(resourceBundle.getString("FileNotExists"));
        } catch (IOException e) {
            console.printError("Ошибка ввода вывода");
        }
    }

    private void printResponse(Response response){
        switch (response.getStatus()){
            case OK -> {
                if ((Objects.isNull(response.getCollection()))) {
                    console.println(response.getResponse());
                } else {
                    console.println(response.getResponse() + "\n" + response.getCollection().toString());
                }
            }
            case ERROR -> console.printError(response.getResponse());
            case WRONG_ARGUMENTS -> console.printError("Неверное использование команды!");
            default -> {}
        }
    }

    public static void pushFile(String path) throws FileNotFoundException{
        pathQueue.push(new File(path).getAbsolutePath());
        fileReaders.push(new BufferedReader(new InputStreamReader(new FileInputStream(path))));
    }

    public static File getFile() {
        return new File(pathQueue.getFirst());
    }

    public static String readLine() throws IOException {
        return fileReaders.getFirst().readLine();
    }
    public static void popFile() throws IOException {
        fileReaders.getFirst().close();
        fileReaders.pop();
        if(pathQueue.size() >= 1) {
            pathQueue.pop();
        }
    }

    public static void popRecursion(){
        if(pathQueue.size() >= 1) {
            pathQueue.pop();
        }
    }

    public static boolean fileRepeat(String path){
        return pathQueue.contains(new File(path).getAbsolutePath());
    }

    @Override
    public String nextLine() {
        try{
            return readLine();
        } catch (IOException e){
            return "";
        }
    }
}

package utility;

import commandLine.Console;
import commandLine.Printable;
import commandLine.forms.StudyGroupForm;
import commandLine.forms.UserForm;
import dtp.Request;
import dtp.Response;
import dtp.ResponseStatus;
import dtp.User;
import exceptions.ExceptionInFileMode;
import exceptions.ExitObliged;
import exceptions.InvalidForm;
import exceptions.LoginDuringExecuteFail;
import models.StudyGroup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

/**
 * Класс обработки пользовательского ввода
 * @author azat2202
 */
public class RuntimeManager {
    private final Printable console;
    private final Scanner userScanner;
    private final Client client;
    private User user = null;

    public RuntimeManager(Printable console, Scanner userScanner, Client client) {
        this.console = console;
        this.userScanner = userScanner;
        this.client = client;
    }

    /**
     * Перманентная работа с пользователем и выполнение команд
     */
    public void interactiveMode(){
        while (true) {
            try{
                if (Objects.isNull(user)) {
                    Response response = null;
                    boolean isLogin = true;
                    do {
                        if(!Objects.isNull(response)) {
                            console.println( (isLogin)
                                    ? "Такой связки логин-пароль нет, попробуйте снова"
                                    : "Этот логин уже занят, попробуйте снова!");
                        }
                        UserForm userForm = new UserForm(console);
                        isLogin = userForm.askIfLogin();
                        user = new UserForm(console).build();
                        if (isLogin) {
                            response = client.sendAndAskResponse(new Request("ping", "", user));
                        } else {
                            response = client.sendAndAskResponse(new Request("register", "", user));
                        }
                    } while (response.getStatus() != ResponseStatus.OK);
                    console.println("Вы успешно зашли в аккаунт");
                }
                if (!userScanner.hasNext()) throw new ExitObliged();
                String[] userCommand = (userScanner.nextLine().trim() + " ").split(" ", 2); // прибавляем пробел, чтобы split выдал два элемента в массиве
                Response response = client.sendAndAskResponse(new Request(userCommand[0].trim(), userCommand[1].trim(), user));
                this.printResponse(response);
                switch (response.getStatus()){
                    case ASK_OBJECT -> {
                        StudyGroup studyGroup = new StudyGroupForm(console).build();
                        if(!studyGroup.validate()) throw new InvalidForm();
                        Response newResponse = client.sendAndAskResponse(
                                new Request(
                                userCommand[0].trim(),
                                userCommand[1].trim(),
                                user,
                                studyGroup));
                        if (newResponse.getStatus() != ResponseStatus.OK){
                            console.printError(newResponse.getResponse());
                        }
                        else {
                            this.printResponse(newResponse);
                        }
                    }
                    case EXIT -> throw new ExitObliged();
                    case EXECUTE_SCRIPT -> {
                        Console.setFileMode(true);
                        this.fileExecution(response.getResponse());
                        Console.setFileMode(false);
                    }
                    case LOGIN_FAILED -> {
                        console.printError("Ошибка с вашим аккаунтом. Зайдите в него снова");
                        this.user = null;
                    }
                    default -> {}
                }
            } catch (InvalidForm err){
                console.printError("Поля не валидны! Объект не создан");
            } catch (NoSuchElementException exception) {
                console.printError("Пользовательский ввод не обнаружен!");
                console.println(ConsoleColors.toColor("До свидания!", ConsoleColors.YELLOW));
                return;
            } catch (ExitObliged exitObliged){
                console.println(ConsoleColors.toColor("До свидания!", ConsoleColors.YELLOW));
                return;
            } catch (LoginDuringExecuteFail e) {
                console.printError("Во время исполнения скрипта произошла ошибка с вашим аккаунтом. Пожалуйста, войдите в него снова");
            }
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

    private void fileExecution(String args) throws ExitObliged, LoginDuringExecuteFail {
        if (args == null || args.isEmpty()) {
            console.printError("Путь не распознан");
            return;
        }
        else console.println(ConsoleColors.toColor("Путь получен успешно", ConsoleColors.PURPLE));
        args = args.trim();
        try {
            ExecuteFileManager.pushFile(args);
            for (String line = ExecuteFileManager.readLine(); line != null; line = ExecuteFileManager.readLine()) {
                String[] userCommand = (line + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                if (userCommand[0].isBlank()) return;
                if (userCommand[0].equals("execute_script")){
                    if(ExecuteFileManager.fileRepeat(userCommand[1])){
                        console.printError("Найдена рекурсия по пути " + new File(userCommand[1]).getAbsolutePath());
                        continue;
                    }
                }
                console.println(ConsoleColors.toColor("Выполнение команды " + userCommand[0], ConsoleColors.YELLOW));
                Response response = client.sendAndAskResponse(new Request(userCommand[0].trim(), userCommand[1].trim(), user));
                this.printResponse(response);
                switch (response.getStatus()){
                    case ASK_OBJECT -> {
                        StudyGroup studyGroup;
                        try{
                            studyGroup = new StudyGroupForm(console).build();
                            if (!studyGroup.validate()) throw new ExceptionInFileMode();
                        } catch (ExceptionInFileMode err){
                            console.printError("Поля в файле не валидны! Объект не создан");
                            continue;
                        }
                       Response newResponse = client.sendAndAskResponse(
                                new Request(
                                        userCommand[0].trim(),
                                        userCommand[1].trim(),
                                        user,
                                        studyGroup));
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
                        ExecuteFileManager.popRecursion();
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
            console.printError("Такого файла не существует");
        } catch (IOException e) {
            console.printError("Ошибка ввода вывода");
        }
    }
}

package commandLine.commands;

import exceptions.ExitObliged;

/**
 * Команда 'exit'
 * завершить программу (без сохранения в файл)
 */
public class Exit extends Command{
    public Exit(){
        super("exit", ": завершить программу (без сохранения в файл)");
    }

    /**
     * Исполнить команду
     * @param args аргументы команды
     * @throws ExitObliged нужен выход из программы
     */
    @Override
    public void execute(String args) throws ExitObliged{
        throw new ExitObliged();
    }
}

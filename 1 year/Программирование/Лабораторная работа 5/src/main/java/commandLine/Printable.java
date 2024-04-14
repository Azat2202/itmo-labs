package commandLine;

/**
 * Интерфейс объединяющий способы вывода
 */
public interface Printable {
    void println(String a);
    void print(String a);
    void printError(String a);
}

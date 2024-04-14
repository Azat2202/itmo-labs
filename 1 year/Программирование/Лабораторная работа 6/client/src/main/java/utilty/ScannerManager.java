package utilty;

import java.util.Scanner;

/**
 * Класс хранящий сканер для программы
 */
public class ScannerManager {
    public static Scanner userScanner = new Scanner(System.in);

    public static Scanner getUserScanner() {
        return userScanner;
    }
}
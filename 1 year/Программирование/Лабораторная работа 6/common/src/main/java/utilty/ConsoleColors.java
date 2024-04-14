package utilty;

/**
 * Класс для раскрашивания вывода в консоль
 */
public enum ConsoleColors {
    BLACK("\u001B[30m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m"),
    CYAN("\u001B[36m"),
    RESET("\u001B[0m"),
    WHITE("\u001B[37m");

    private final String title;

    ConsoleColors(String title) {
        this.title = title;
    }

    /**
     * Основной метод раскрашивания текста
     * @param s строка которую нужно покрасить
     * @param color значение цвета
     * @return цветная строка для вывода в консоль
     */
    public static String toColor(String s, ConsoleColors color){
        return color + s + ConsoleColors.RESET;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}

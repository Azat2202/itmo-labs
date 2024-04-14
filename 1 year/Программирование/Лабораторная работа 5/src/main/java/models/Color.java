package models;

/**
 * Перечисление различных цветов
 * @author azat2202
 */
public enum Color {
    GREEN,
    RED,
    ORANGE,
    WHITE,
    BROWN;
    /**
     * @return перечисляет в строке все элементы Enum
     */
    public static String names() {
        StringBuilder nameList = new StringBuilder();
        for (var forms : values()) {
            nameList.append(forms.name()).append("\n");
        }
        return nameList.substring(0, nameList.length()-1);
    }
}
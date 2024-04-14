package models;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * Класс координат
 * @author azat2202
 */
public class Coordinates implements Validator, Serializable, Comparable<Coordinates> {
    private float x; //Значение поля должно быть больше -206
    private Double y; //Максимальное значение поля: 463, Поле не может быть null

    public Coordinates(float x, Double y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    @Override
    public int compareTo(Coordinates o) {
        if (Objects.isNull(o)) return 1;
        return Double.compare((Math.pow(this.getX(), 2) + Math.pow(this.getY(), 2)),
                (Math.pow(o.getX(), 2) + Math.pow(o.getY(), 2)));
    }

    public float getRadius(){
        return (float) (Math.pow(getX(), 2) + Math.pow(getY(), 2));
    }

    /**
     * Валидирует правильность полей.
     * @return true, если все верно, иначе false
     */
    @Override
    public boolean validate() {
        if (this.x <= -206) return false;
        return !(this.y == null || this.y > 463);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Float.compare(that.x, x) == 0 && y.equals(that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}
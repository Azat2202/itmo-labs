package models;

import java.io.Serializable;
import java.util.Objects;

/**
 * Класс локации
 * @author azat2202
 */
public class Location implements Validator, Serializable {
    private double x;
    private long y;
    private String name; //Строка не может быть пустой, Поле может быть null

    public Location(double x, long y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public Coordinates getCoordinates(){
        return new Coordinates((float) this.getX(), (double) this.getY());
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }

    public float getRadius(){
        return (float) (Math.pow(getX(), 2) + Math.pow(getY(), 2));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Валидирует правильность полей.
     * @return true, если все верно, иначе false
     */
    @Override
    public boolean validate() {
        return !this.name.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (Double.compare(location.x, x) != 0) return false;
        if (y != location.y) return false;
        return Objects.equals(name, location.name);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) (y ^ (y >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", name='" + name + '\'' +
                '}';
    }
}

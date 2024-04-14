package models;

import utilty.ConsoleColors;

import java.io.Serializable;
import java.util.Objects;

/**
 * Класс человека
 * @author azat2202
 */
public class Person implements Validator, Serializable {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private int weight; //Значение поля должно быть больше 0
    private Color eyeColor; //Поле может быть null
    private Color hairColor; //Поле может быть null
    private Country nationality; //Поле может быть null
    private Location location; //Поле может быть null

    public Person(String name, int weight, Color eyeColor, Color hairColor, Country nationality, Location location) {
        this.name = name;
        this.weight = weight;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
        this.nationality = nationality;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Color getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(Color eyeColor) {
        this.eyeColor = eyeColor;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Валидирует правильность полей.
     * @return true, если все верно, иначе false
     */
    @Override
    public boolean validate() {
        if (this.name == null || this.name.isEmpty()) return false;
        return this.weight > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (weight != person.weight) return false;
        if (!name.equals(person.name)) return false;
        if (eyeColor != person.eyeColor) return false;
        if (hairColor != person.hairColor) return false;
        if (nationality != person.nationality) return false;
        return Objects.equals(location, person.location);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + weight;
        result = 31 * result + (eyeColor != null ? eyeColor.hashCode() : 0);
        result = 31 * result + (hairColor != null ? hairColor.hashCode() : 0);
        result = 31 * result + (nationality != null ? nationality.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Person{" + "\n" +
                ConsoleColors.toColor("name=", ConsoleColors.CYAN) + name  + "\n" +
                ConsoleColors.toColor("weight=", ConsoleColors.CYAN) + weight + "\n" +
                ConsoleColors.toColor("eyeColor=", ConsoleColors.CYAN) + eyeColor + "\n" +
                ConsoleColors.toColor("hairColor=", ConsoleColors.CYAN) + hairColor + "\n" +
                ConsoleColors.toColor("nationality=", ConsoleColors.CYAN) + nationality + "\n" +
                ConsoleColors.toColor("location=", ConsoleColors.CYAN) + location + "\n" +
                '}';
    }
}
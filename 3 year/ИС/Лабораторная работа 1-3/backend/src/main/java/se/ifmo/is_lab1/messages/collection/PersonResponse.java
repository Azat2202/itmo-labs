package se.ifmo.is_lab1.messages.collection;

import lombok.Data;
import se.ifmo.is_lab1.models.Location;
import se.ifmo.is_lab1.models.enums.Color;
import se.ifmo.is_lab1.models.enums.Country;

@Data
public class PersonResponse {
    private Long id;

    private String name; //Поле не может быть null, Строка не может быть пустой

    private Color eyeColor; //Поле не может быть null

    private Color hairColor; //Поле может быть null

    private Location location; //Поле не может быть null

    private double weight; //Значение поля должно быть больше 0

    private Country nationality; //Поле может быть null
}

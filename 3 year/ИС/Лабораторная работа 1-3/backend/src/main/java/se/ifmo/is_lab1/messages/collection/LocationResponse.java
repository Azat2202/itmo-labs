package se.ifmo.is_lab1.messages.collection;

import lombok.Data;

@Data
public class LocationResponse {
    private Long id;

    private float x;

    private Double y; //Поле не может быть null

    private Float z; //Поле не может быть null

    private String name; //Строка не может быть пустой, Поле может быть null
}

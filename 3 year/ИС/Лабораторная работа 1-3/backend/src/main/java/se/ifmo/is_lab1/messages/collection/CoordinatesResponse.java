package se.ifmo.is_lab1.messages.collection;

import lombok.Data;

@Data
public class CoordinatesResponse {
    private Long id;

    private float x;

    private Long y; //Поле не может быть null
}

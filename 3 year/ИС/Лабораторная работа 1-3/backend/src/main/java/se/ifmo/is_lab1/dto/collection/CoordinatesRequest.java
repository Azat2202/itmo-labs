package se.ifmo.is_lab1.dto.collection;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CoordinatesRequest {
    @NotNull
    private float x;

    @NotNull
    private Long y; //Поле не может быть null
}

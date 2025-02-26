package se.ifmo.is_lab1.dto.collection;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocationRequest {
    @NotNull
    private float x;

    @NotNull
    private Double y; //Поле не может быть null

    @NotNull
    private Float z; //Поле не может быть null

    @NotBlank
    private String name; //Строка не может быть пустой, Поле может быть null
}

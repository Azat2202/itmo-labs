package se.ifmo.is_lab1.dto.batch;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocationBatchRequest {
    private Long id;

    @NotNull
    private Float x;

    @NotNull
    private Double y; //Поле не может быть null

    @NotNull
    private Float z; //Поле не может быть null

    @NotBlank
    private String name; //Строка не может быть пустой, Поле может быть null

    public Boolean validate() {
        return (id != null) || (x != null &&
                y != null &&
                z != null &&
                name != null &&
                !name.isEmpty() &&
                !name.isBlank());
    }
}

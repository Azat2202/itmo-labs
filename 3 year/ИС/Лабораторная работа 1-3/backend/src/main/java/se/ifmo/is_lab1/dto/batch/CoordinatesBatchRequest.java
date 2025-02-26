package se.ifmo.is_lab1.dto.batch;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CoordinatesBatchRequest {
    private Long id;

    private Float x;

    private Long y; //Поле не может быть null

    public Boolean validate() {
        return (id != null) || (x != null && y != null);
    }
}

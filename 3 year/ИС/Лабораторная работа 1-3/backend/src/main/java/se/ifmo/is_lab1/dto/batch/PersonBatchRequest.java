package se.ifmo.is_lab1.dto.batch;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import se.ifmo.is_lab1.models.enums.Color;
import se.ifmo.is_lab1.models.enums.Country;

import java.io.IOException;

@Data
public class PersonBatchRequest {
    private Long id;

    private String name; //Поле не может быть null, Строка не может быть пустой

    private Color eyeColor; //Поле не может быть null

    private Color hairColor; //Поле может быть null

    private LocationBatchRequest location; //Поле не может быть null

    private Double weight; //Значение поля должно быть больше 0

    private Country nationality; //Поле может быть null

    public Boolean validate() {
        return (id != null) || (name != null &&
                !name.isEmpty() &&
                !name.isBlank() &&
                eyeColor != null &&
                location.validate() &&
                weight != null &&
                weight > 0);
    }

}


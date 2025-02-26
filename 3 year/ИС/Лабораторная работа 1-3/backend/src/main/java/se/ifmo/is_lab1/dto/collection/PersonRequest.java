package se.ifmo.is_lab1.dto.collection;

import com.fasterxml.jackson.core.JacksonException;
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
public class PersonRequest {
    @NotNull
    @NotBlank
    private String name; //Поле не может быть null, Строка не может быть пустой

    @NotNull
    private Color eyeColor; //Поле не может быть null

    @JsonDeserialize(using = ColorDesializer.class)
    private Color hairColor; //Поле может быть null

    @NotNull
    private Long locationId; //Поле не может быть null

    @NotNull
    @Positive
    private double weight; //Значение поля должно быть больше 0

    private Country nationality; //Поле может быть null
}

class ColorDesializer extends JsonDeserializer<Color> {

    @Override
    public Color deserialize(JsonParser jsonParser, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        String value = jsonParser.getValueAsString();
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Color.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
package com.example.is_coursework.dto.responses;

import com.example.is_coursework.models.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
public class GenerateFactResponse {
    private List<Bag> bags;
    private List<BodyType> bodyTypes;
    private List<Equipment> equipments;
    private List<Health> healths;
    private List<Hobby> hobbies;
    private List<Phobia> phobiases;
    private List<Profession> professions;
    private List<Trait> traits;
}

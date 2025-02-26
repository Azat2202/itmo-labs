package com.example.is_coursework.dto.responses;

import com.example.is_coursework.models.*;
import com.example.is_coursework.models.enums.SexType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
public class CharacterResponse {
    private Long id;
    private String name;
    private Integer age;
    private SexType sex;
    private String notes;
    private Boolean isActive;
    private User user;
    private BodyType bodyType;
    private Health health;
    private Trait trait;
    private Hobby hobby;
    private Profession profession;
    private Phobia phobia;
    private Equipment equipment;
    private Bag bag;
    private OpenedFacts openedFacts;
}

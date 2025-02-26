package com.example.is_coursework.messages;

import com.example.is_coursework.models.*;
import com.example.is_coursework.models.enums.SexType;
import lombok.Data;

import java.util.List;

@Data
public class CharacterPrivateMessage {
    private Long id;

    private String name;

    private Integer age;

    private SexType sex;

    private String notes;

    private Boolean isActive;

    private User user;

    private OpenedFacts openedFacts;
}

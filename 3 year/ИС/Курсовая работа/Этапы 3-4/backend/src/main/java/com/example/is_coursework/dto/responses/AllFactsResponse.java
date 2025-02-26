package com.example.is_coursework.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;


@Data
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
public class AllFactsResponse {
    private String name;
    private String age;
    private String sex;
    private String bag;
    private String bodyType;
    private String equipment;
    private String health;
    private String hobby;
    private String phobia;
    private String profession;
    private String trait;
    private String notes;
}

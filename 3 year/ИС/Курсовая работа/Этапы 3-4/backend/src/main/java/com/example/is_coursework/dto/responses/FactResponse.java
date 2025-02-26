package com.example.is_coursework.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
public class FactResponse {
    private String name;
}

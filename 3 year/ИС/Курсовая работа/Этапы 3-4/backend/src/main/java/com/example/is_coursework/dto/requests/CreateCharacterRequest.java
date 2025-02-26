package com.example.is_coursework.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCharacterRequest {
    @NotNull
    @NotBlank
    @NotEmpty
    private String name;

    @NotNull
    private String notes;

    @NotNull
    private Long bodyTypeId;

    @NotNull
    private Long healthId;

    @NotNull
    private Long traitId;

    @NotNull
    private Long hobbyId;

    @NotNull
    private Long professionId;

    @NotNull
    private Long phobiaId;

    @NotNull
    private Long equipmentId;

    @NotNull
    private Long bagId;

    @NotNull
    private Long roomId;
}


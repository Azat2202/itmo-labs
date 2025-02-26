package com.example.is_coursework.controllers;

import com.example.is_coursework.dto.requests.CreateCharacterRequest;
import com.example.is_coursework.dto.requests.OpenedFactsRequest;
import com.example.is_coursework.dto.responses.*;
import com.example.is_coursework.interfaces.CurrentUser;
import com.example.is_coursework.models.User;
import com.example.is_coursework.models.enums.FactType;
import com.example.is_coursework.services.CharacterService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/characters")
public class CharacterController {
    private final CharacterService characterService;

    @PostMapping("/create")
    @Operation(summary = "Выбор характеристик и остальных полей")
    public CharacterResponse createCharacter(@CurrentUser User user, @RequestBody CreateCharacterRequest createCharacterRequest) {
        return characterService.createCharacter(createCharacterRequest, user);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение персонажа по id")
    public CharacterResponse getCharacterById(@PathVariable Long id) {
        return characterService.getCharacterById(id);
    }

    @PutMapping("/open_fact/{character_id}/{factType}")
    @Operation(summary = "Открыть факт")
    public FactResponse openFact(@PathVariable Long character_id, @PathVariable FactType factType) {
        return characterService.openFact(character_id, factType);
    }

    @GetMapping("/get_opened")
    public OpenedFactsResponse getOpenedFacts(OpenedFactsRequest openedFactsRequest) {
        return characterService.getOpenFacts(openedFactsRequest);
    }

    @GetMapping("/get_all_facts/{character_id}")
    public AllFactsResponse getAllFacts(@PathVariable Long character_id) {
        return characterService.getAllFacts(character_id);
    }

    @GetMapping("/generate_facts/{roomId}")
    public GenerateFactResponse generateFact(@CurrentUser User user, @PathVariable Long roomId) {
        return characterService.generateFact(user, roomId);
    }
}

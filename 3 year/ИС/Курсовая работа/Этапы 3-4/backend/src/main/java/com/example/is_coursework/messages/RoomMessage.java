package com.example.is_coursework.messages;

import com.example.is_coursework.models.OpenedFacts;
import lombok.Data;

import java.util.List;

@Data
public class RoomMessage {
    private Long id;

    private BunkerMessage bunker;

    private CataclysmMessage cataclysm;

    private String joinCode;

    private Boolean isStarted;

    private Boolean isClosed;

    private List<CharacterPrivateMessage> characters;
}
package com.example.is_coursework.messages;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VoteMessage {
    private Long id;
    private CharacterPrivateMessage targetCharacter;
}

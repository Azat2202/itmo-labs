package com.example.is_coursework.messages;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Builder(toBuilder = true)
@Data
public class PollMessage{
    private Long id;

    private Integer roundNumber;

    private ZonedDateTime creationTime;

    private Boolean isOpen;

    private CharacterPrivateMessage targetCharacter;

    private List<VoteMessage> votes;
}

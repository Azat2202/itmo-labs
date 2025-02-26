package com.example.is_coursework.models;

import com.example.is_coursework.messages.CharacterPrivateMessage;
import com.example.is_coursework.messages.VoteMessage;
import jakarta.persistence.*;
import lombok.*;
import org.modelmapper.ModelMapper;

@Entity(name = "votes")
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"room", "character", "targetCharacter", "poll"})
@AllArgsConstructor
@Builder(toBuilder = true)
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "target_character_id", nullable = false)
    private Character targetCharacter;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;

    public VoteMessage toVoteMessage(){
        ModelMapper modelMapper = new ModelMapper();
        return VoteMessage.builder()
                .id(id)
                .targetCharacter(modelMapper.map(targetCharacter, CharacterPrivateMessage.class))
                .build();
    }
}

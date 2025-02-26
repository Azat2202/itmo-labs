package com.example.is_coursework.models;

import com.example.is_coursework.messages.CharacterPrivateMessage;
import com.example.is_coursework.messages.PollMessage;
import jakarta.persistence.*;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Entity(name = "polls")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(nullable = false)
    private Integer roundNumber;

    @Column(nullable = false)
    private ZonedDateTime creationTime;

    @Column(nullable = false)
    private Boolean isOpen;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "target_to_kick", nullable = false)
    private Character targetCharacter;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "poll_id")
    private List<Vote> votes;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.creationTime = now.atZone(ZoneId.systemDefault());
    }

    public PollMessage toPollMessage() {
        ModelMapper modelMapper = new ModelMapper();
        PollMessage pollMessage = PollMessage.builder()
                .id(id)
                .roundNumber(roundNumber)
                .creationTime(creationTime)
                .isOpen(isOpen)
                .targetCharacter(
                        targetCharacter != null ? modelMapper.map(targetCharacter, CharacterPrivateMessage.class) : null)
                .build();
        if (!isOpen) {
            pollMessage = pollMessage.toBuilder().votes(votes.stream().map(Vote::toVoteMessage).toList()).build();
        }
        return pollMessage;
    }
}

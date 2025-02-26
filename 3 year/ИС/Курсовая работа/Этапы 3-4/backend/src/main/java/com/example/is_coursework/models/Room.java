package com.example.is_coursework.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bunker_id", nullable = false)
    private Bunker bunker;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cataclysm_id", nullable = false)
    private Cataclysm cataclysm;

    @Column(nullable = false, unique = true)
    private String joinCode;

    @Column(nullable = false)
    private Boolean isStarted;

    @Column(nullable = false)
    private Boolean isClosed;

    @ManyToMany
    @JoinTable(
            name = "character_in_room",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "character_id")
    )
    private List<Character> characters;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admin", nullable = false)
    private User admin;

    @OneToMany(mappedBy = "character_id", fetch = FetchType.LAZY)
    private List<OpenedFacts> openFacts;
}

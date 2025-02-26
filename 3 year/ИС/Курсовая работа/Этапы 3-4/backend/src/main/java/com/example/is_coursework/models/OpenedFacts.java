package com.example.is_coursework.models;

import com.example.is_coursework.models.enums.SexType;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "open_facts")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class OpenedFacts {
    @Id
    @Column(updatable = false, nullable = false)
    private Long character_id;

    @Column(nullable = false)
    private String bodyType;

    @Column(nullable = false)
    private String health;

    @Column(nullable = false)
    private String trait;

    @Column(nullable = false)
    private String hobby;

    @Column(nullable = false)
    private String profession;

    @Column(nullable = false)
    private String phobia;

    @Column(nullable = false)
    private String equipment;

    @Column(nullable = false)
    private String bag;
}

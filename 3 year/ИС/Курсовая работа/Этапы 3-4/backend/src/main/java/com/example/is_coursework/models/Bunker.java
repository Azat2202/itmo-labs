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
public class Bunker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private Integer square;

    @Column(nullable = false)
    private Integer stayDays;

    @Column(nullable = false)
    private Integer foodDays;

    @ManyToMany
    @JoinTable(
            name = "equipment_in_bunker",
            joinColumns = @JoinColumn(name = "bunker_id"),
            inverseJoinColumns = @JoinColumn(name = "equipment_id")
    )
    private List<Equipment> equipments;
}

package com.example.is_coursework.models;

import com.example.is_coursework.models.enums.SexType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "characters")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SexType sex;

    @Column(nullable = false)
    private String notes;

    @Column(nullable = false)
    private Boolean isActive;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "body_type_id")
    private BodyType bodyType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "health_id")
    private Health health;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trait_id")
    private Trait trait;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hobby_id")
    private Hobby hobby;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profession_id")
    private Profession profession;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "phobia_id")
    private Phobia phobia;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bag_id")
    private Bag bag;

    @ManyToMany
    @JoinTable(
            name = "character_in_room",
            joinColumns = @JoinColumn(name = "character_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    private List<Room> room;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private OpenedFacts openedFacts;


    public Boolean validateChoosable() {
        return bodyType != null && health != null && trait != null && hobby != null && profession != null && phobia != null && equipment != null && bag != null;
    }

}

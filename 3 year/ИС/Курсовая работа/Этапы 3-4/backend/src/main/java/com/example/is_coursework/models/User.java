package com.example.is_coursework.models;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "users")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String username;
}

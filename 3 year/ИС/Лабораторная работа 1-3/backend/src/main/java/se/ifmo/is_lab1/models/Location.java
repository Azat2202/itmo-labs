package se.ifmo.is_lab1.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private float x;

    @Column(nullable = false)
    private Double y; //Поле не может быть null

    @Column(nullable = false)
    private Float z; //Поле не может быть null

    @Column(nullable = false)
    @NotBlank
    private String name; //Строка не может быть пустой, Поле может быть null

    @OneToMany(mappedBy = "location")
    @JsonIgnore
    private List<Person> persons;
}
package se.ifmo.is_lab1.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import se.ifmo.is_lab1.models.enums.FormOfEducation;
import se.ifmo.is_lab1.models.enums.Semester;

import java.time.Instant;
import java.time.ZoneId;


@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class StudyGroup{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @Column(nullable = false)
    private String name; //Поле не может быть null, Строка не может быть пустой

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Coordinates coordinates; //Поле не может быть null

    @Column(nullable = false)
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @Column(nullable = false)
    private Integer studentsCount; //Значение поля должно быть больше 0, Поле не может быть null

    @Column(nullable = false)
    private Integer expelledStudents; //Значение поля должно быть больше 0, Поле не может быть null

    @Column
    private int transferredStudents; //Значение поля должно быть больше 0

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FormOfEducation formOfEducation; //Поле не может быть null

    @Column
    private Integer shouldBeExpelled; //Значение поля должно быть больше 0, Поле может быть null

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Semester semester; //Поле не может быть null

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Person groupAdmin; //Поле может быть null

    @Column
    @NotNull
    private Boolean isEditable = Boolean.FALSE;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private User user;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.creationDate = now.atZone(ZoneId.systemDefault());
    }
}

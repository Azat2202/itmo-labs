package se.ifmo.is_lab1.models;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.ZoneId;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class ObjectAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String tableName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private User user;

    @Column(nullable = false)
    private java.time.ZonedDateTime editDate;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.editDate = now.atZone(ZoneId.systemDefault());
    }
}

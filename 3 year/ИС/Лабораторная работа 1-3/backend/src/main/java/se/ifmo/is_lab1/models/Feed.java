package se.ifmo.is_lab1.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.time.ZoneId;

@Entity(name = "feeds")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class Feed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private java.time.ZonedDateTime creationDate;

    @Column
    private String feedUrl;

    @Column
    private Integer batchSize;

    @Column
    private Boolean isSuccessful;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.creationDate = now.atZone(ZoneId.systemDefault());
    }
}

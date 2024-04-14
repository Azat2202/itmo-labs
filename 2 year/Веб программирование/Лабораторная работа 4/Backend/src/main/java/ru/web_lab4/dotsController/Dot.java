package ru.web_lab4.dotsController;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@NoArgsConstructor
public class Dot implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    private Float x;
    private Float y;
    private Float r;
    private Boolean status;
    private String time;
    private Long scriptTime;
    private String ownerLogin;

    public Dot(float x, float y, float r, String ownerLogin){
        this.x = x;
        this.y = y;
        this.r = r;
        this.status = checkStatus(x, y, r);
        this.time = DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now());
        this.ownerLogin = ownerLogin;
    }

    public static boolean validateInput(float x, float y, float r){
        if (x > 2 || x < -2) return false;
        if (y > 5 || y < -5) return false;
        return !(r > 2) && !(r < 0);
    }

    public static boolean checkStatus(float x, float y, float r){
        if (x >= 0 && y >= 0) return (x <= r) && (y <= r / 2);
        if (x >= 0 && y <= 0) return (y >= x - r);
        if (x <= 0 && y <= 0) return (x * x + y * y <= r * r);
        return false;
    }
}

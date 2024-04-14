package models;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.io.Serializable;

@Named("abstractPoint")
@ApplicationScoped
public class Point implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    private float x = 0;
    private float y = 0;
    private float r = 1;
    private Boolean status;
    private String time;
    private long scriptTime;

    public Point() {
    }

    public Point(float x, float y, float r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public Point(Long id, float x, float y, float r, Boolean status, String time, long scriptTime) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.r = r;
        this.status = status;
        this.time = time;
        this.scriptTime = scriptTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getStatusWord(){
        return (status)
                ? "Попадание"
                : "Промах";
    }

    public String getStatusString(){
        return (status)
                ? "попадание"
                : "промах";
    }

    public String getStatusHTMLClass(){
        return (status)
                ? "green-status"
                : "red-status";
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getScriptTime() {
        return scriptTime;
    }

    public void setScriptTime(long scriptTime) {
        this.scriptTime = scriptTime;
    }

    @Override
    public String toString() {
        return x + " " + y + " " + r;
    }
}

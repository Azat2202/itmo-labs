package models;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
//import jakarta.p
import org.primefaces.event.SlideEndEvent;

import java.io.Serializable;

@Named("abstractPoint")
@ApplicationScoped
public class Point implements Serializable {
    private long id;
    private double x, y, r;
    private String currentTime;
    private String executionTime;
    private boolean success;
    public Point() {
    }

    public Point(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public Point(long id, double x, double y, double r, boolean success, String currentTime, String executionTime) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.r = r;
        this.currentTime = currentTime;
        this.executionTime = executionTime;
        this.success = success;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public void onSlideEndR(SlideEndEvent event) {
        this.r = event.getValue();
    }

    public void onSlideEndX(SlideEndEvent event) {
        this.x = event.getValue();
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

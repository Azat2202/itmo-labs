package beans;

import java.io.Serializable;

public class Point implements Serializable {
    private Double x;
    private Double y;
    private Integer r;
    private Boolean status;
    private String time;
    private long scriptTime;

    public Point(Double x, Double y, Integer r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Integer getR() {
        return r;
    }

    public void setR(Integer r) {
        this.r = r;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getStringStatus(){
        return (status)
                ? "Попадание"
                : "Промах";
    }

    public String getHTMLClass(){
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
        return "{ " + this.x + ", " + this.y + ", " + this.r + " }";
    }
}
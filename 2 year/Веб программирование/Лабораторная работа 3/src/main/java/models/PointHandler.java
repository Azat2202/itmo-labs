package models;

import database.DatabaseHandler;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import validators.PointValidator;

import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.Map;

@Named("pointHandler")
@ApplicationScoped
public class PointHandler implements Serializable {

    private Point point = new Point();
    private LinkedList<Point> points = new LinkedList<>();

    public LinkedList<Point> getPoints() {
        return points;
    }

    @PostConstruct
    public void loadPointsFromDb(){
        this.points = DatabaseHandler.getDatabaseManager().loadCollection();
    }

    public void add(){
        long timer = System.nanoTime();
        point.setTime(DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));
        point.setStatus(PointValidator.isHit(point.getX(), point.getY(), point.getR()));
        point.setScriptTime((long) ((System.nanoTime() - timer) * 0.01));

        this.addPoint(point);
        point = new Point(point.getX(), point.getY(), point.getR());
    }

    public void clear(){
//        System.out.println("clear");
//        try {
//            DatabaseHandler.getDatabaseManager().clearCollection();
//            this.points.clear();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    public void addFromJS(){
        long timer = System.nanoTime();
        final Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        params.values().forEach(System.out::println);

        try {
            float x = Float.parseFloat(params.get("x"));
            float y = Float.parseFloat(params.get("y"));
            float r = Float.parseFloat(params.get("r"));

            final Point attemptBean = new Point(x, y, r);
            attemptBean.setTime(DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));
            attemptBean.setStatus(PointValidator.isHit(x, y, r));
            attemptBean.setScriptTime((long) ((System.nanoTime() - timer) * 0.01));
            this.addPoint(attemptBean);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            System.out.println(e.getLocalizedMessage());
        }
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public void addPoint(Point point){
        DatabaseHandler.getDatabaseManager().addPoint(point);
        this.points.addFirst(point);
    }

    public void setPoints(LinkedList<Point> points) {
        this.points = points;
    }
}

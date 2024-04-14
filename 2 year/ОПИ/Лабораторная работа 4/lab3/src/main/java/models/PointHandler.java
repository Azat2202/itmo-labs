package models;

import database.DatabaseHandler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.event.Observes;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import utils.MBeanRegistryUtil;
import validators.PointValidator;

import javax.management.AttributeChangeNotification;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.Map;

@Named
@ApplicationScoped
public class PointHandler extends NotificationBroadcasterSupport implements Serializable, PointHandlerMBean {

    private Point point = new Point();
    private LinkedList<Point> points = new LinkedList<>();
    private long sequenceNumber = 1;

    public LinkedList<Point> getPoints() {
        return points;
    }

    public void init(@Observes @Initialized(ApplicationScoped.class) Object unused) {
        this.points = DatabaseHandler.getDatabaseManager().loadCollection();
        MBeanRegistryUtil.registerBean(this, "main");
    }

    public void destroy(@Observes @Destroyed(ApplicationScoped.class) Object unused) {
        MBeanRegistryUtil.unregisterBean(this);
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
//        params.values().forEach(System.out::println);

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
//            System.out.println(e.getCause());
//            System.out.println(e.getMessage());
//            System.out.println(e.getLocalizedMessage());
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
        if(!point.getStatus()){
            Notification notification = new Notification(
                    "Outside of area",
                    this,
                    sequenceNumber++,
                    "new Point is out of area!"
            );
            sendNotification(notification);
        }
    }

    public void setPoints(LinkedList<Point> points) {
        this.points = points;
    }

    @Override
    public long getDotsCount() {
        return this.points.size();
    }

    @Override
    public long getSuccessDotsCount() {
        return this.points.stream()
                .filter(Point::getStatus)
                .count();
    }

    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        String[] types = new String[] { AttributeChangeNotification.ATTRIBUTE_CHANGE };
        String name = AttributeChangeNotification.class.getName();
        String description = "The point is outside of area.";
        MBeanNotificationInfo info = new MBeanNotificationInfo(types, name, description);
        return new MBeanNotificationInfo[] { info };
    }
}

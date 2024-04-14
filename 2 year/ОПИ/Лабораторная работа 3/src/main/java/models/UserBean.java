package models;

import database.DatabaseHandler;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import utils.AreaValidator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;


@Named("userBean")
@ApplicationScoped
public class UserBean implements Serializable {
    private Point point = new Point();
    private ArrayList<Point> requests;
    @PostConstruct
    public void loadPointsFromDb(){
        this.requests = DatabaseHandler.getDatabaseManager().loadCollection();
    }

    public void add(){
        long timer = System.nanoTime();
        point.setCurrentTime(DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));
        point.setSuccess(AreaValidator.checkArea(point));
        point.setExecutionTime(String.valueOf(String.format("%.2f", ((System.nanoTime() - timer) * 0.000001))));

        this.addPoint(point);
        point = new Point(point.getX(), point.getY(), point.getR());
    }

    public void addPoint(Point point){
        DatabaseHandler.getDatabaseManager().addPoint(point);
        this.requests.add(0, point);
    }

    public void clearRequests() {
        DatabaseHandler.getDatabaseManager().clearCollection();
        this.requests = new ArrayList<>();
    }

    public void addFromJS(){
        long timer = System.nanoTime();
        final Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        params.values().forEach(System.out::println);

        try {
            double x = Double.parseDouble(params.get("x"));
            double y = Double.parseDouble(params.get("y"));
            double r = Double.parseDouble(params.get("r"));

            final Point attemptBean = new Point(x, y, r);
            attemptBean.setCurrentTime(DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));
            attemptBean.setSuccess(AreaValidator.checkArea(attemptBean));
            attemptBean.setExecutionTime(String.valueOf(String.format("%.2f", ((System.nanoTime() - timer) * 0.000001))));
            this.addPoint(attemptBean);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getCause());
            System.out.println(e.getMessage());
            System.out.println(e.getLocalizedMessage());
        }
    }

    public ArrayList<Point> getRequests() {
        return requests;
    }

    public void setRequests(ArrayList<Point> requests) {
        this.requests = requests;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "point=" + point +
                ", requests=" + requests +
                '}';
    }
}

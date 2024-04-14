package validators;

import beans.Point;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Objects;

public class Validator implements IValidator {
    private HttpServletRequest req;
    private Double x;
    private Double y;
    private Integer r;
    public Validator(HttpServletRequest req){
        this.req = req;
    }

    public Point getPoint(){
        if (x == null || y == null || r == null){
            this.getStatus();
        }
        return new Point(x, y, r);
    }

    public boolean checkFormat(){
        return !req.getParameter("X").isEmpty() && !req.getParameter("Y").isEmpty() && !req.getParameter("R").isEmpty();
    }

    public HitStatus getStatus(){
        if(!validateX(req.getParameter("X")) || !validateY(req.getParameter("Y")) || !validateR(req.getParameter("R"))){
            return HitStatus.NOT_VALIDATED;
        }
        if(this.isHit()){
            return HitStatus.HIT;
        }
        return HitStatus.MISS;
    }

    @Override
    public boolean validateX(String xString) {
        if (Objects.isNull(xString) || xString.isEmpty()) return false;
        try {
            this.x = Double.parseDouble(xString);
            return x >= -5 && x <= 3;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean validateY(String yString) {
        if (Objects.isNull(yString) || yString.isEmpty()) return false;
        try {
            this.y = Double.parseDouble(yString);
            return y >= -3 && y <= 3;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean validateR(String rString) {
        if (Objects.isNull(rString) || rString.isEmpty()) return false;
        try {
            this.r = Integer.parseInt(rString);
            return r >= 1 && r <= 5;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean isHit() {
        if(x >= 0 && y >= 0 && y <= (double) r / 2 - x) return true;
        if(x >= 0 && y <= 0 && x*x + y*y <= r*r) return true;
        return x <= 0 && y <= 0 && x >= (double) -r / 2 && y >= - r;
    }
}

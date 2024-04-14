package utils;

import models.Point;

public class AreaValidator {
    public static boolean checkArea(Point point) {
        double x = point.getX();
        double y = point.getY();
        double r = point.getR();
        if (x > 0 && y < 0 && (x * x + y * y > r * r / 4)) return false;
        if (x < 0 && y > 0 && (y > x + r)) return false;
        if (x > 0 && y > 0 && (x > r || y > r / 2)) return false;
        if (x < 0 && y < 0) return false;
        if (x == 0 && (y > r || y < -r)) return false;
        if (y == 0 && (x > r || x < -r)) return false;
        return true;
    }
}

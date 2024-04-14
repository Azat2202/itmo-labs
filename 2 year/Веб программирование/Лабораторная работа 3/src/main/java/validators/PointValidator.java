package validators;

public class PointValidator {
    public static Boolean isHit(float x, float y, float r){
        if(x >= 0 && y >= 0 && x <= r / 2 && y < r) return true;
        if(x <= 0 && y >= 0 && y <= r / 2 + x) return true;
        return x <= 0 && y <= 0 && x * x + y * y <= r * r / 4;
    }
}

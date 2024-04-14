package validators;

import jakarta.servlet.http.HttpServletRequest;

public interface IValidator {
    boolean validateX(String xString);
    boolean validateY(String yString);
    boolean validateR(String rString);
    boolean isHit();
}

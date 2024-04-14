package utils;

import models.Point;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class AreaValidatorTest {
    @Test
    void checkAreaOnRectangle(){
        assertTrue(AreaValidator.checkArea(new Point(2, 0, 2)));
        assertTrue(AreaValidator.checkArea(new Point(2, 1, 2)));
        assertTrue(AreaValidator.checkArea(new Point(1, 1, 2)));
        assertTrue(AreaValidator.checkArea(new Point(0, 1, 2)));
    }

    @Test
    void checkAreaOnTriangle(){
        assertTrue(AreaValidator.checkArea(new Point(0, 2, 2)));
        assertTrue(AreaValidator.checkArea(new Point(-1, 1, 2)));
        assertTrue(AreaValidator.checkArea(new Point(-2, 0, 2)));
        assertTrue(AreaValidator.checkArea(new Point(-1, 0, 2)));
    }

    @Test
    void checkAreaOnCircle(){
        assertTrue(AreaValidator.checkArea(new Point(0, -1, 2)));
        assertTrue(AreaValidator.checkArea(new Point(2, -1.5, 5)));
    }

    @Test
    void checkAreaOnEmptyField(){
        assertFalse(AreaValidator.checkArea(new Point(-1, -1, 2)));
        assertFalse(AreaValidator.checkArea(new Point(2.1, 0, 2)));
        assertFalse(AreaValidator.checkArea(new Point(1, 1.1, 2)));
        assertFalse(AreaValidator.checkArea(new Point(-1.5, 1.5, 2)));
        assertFalse(AreaValidator.checkArea(new Point(2, -2, 2)));
    }

    @Test
    void checkAreaOnEmpty(){
        assertTrue(AreaValidator.checkArea(new Point(0, 0, 2)));
    }

}

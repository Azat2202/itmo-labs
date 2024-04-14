package models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UserBeanTest {
    private static UserBean userBean;
    @BeforeAll
    public static void createUserBean(){
        userBean = new UserBean();
    }

    @Test
    public void addTest() {
        Assertions.assertDoesNotThrow(() -> userBean.addPoint(new Point(1, 1, 3)));
    }
}

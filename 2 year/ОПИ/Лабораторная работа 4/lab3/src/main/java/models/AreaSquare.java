package models;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import utils.MBeanRegistryUtil;



@Named
@ApplicationScoped
public class AreaSquare implements AreaSquareMBean {
    @Inject
    private PointHandler pointHandler;

    public void init(@Observes @Initialized(ApplicationScoped.class) Object unused) {
        MBeanRegistryUtil.registerBean(this, "areasquare");
    }

    public void destroy(@Observes @Destroyed(ApplicationScoped.class) Object unused) {
        MBeanRegistryUtil.unregisterBean(this);
    }

    @Override
    public double getSquare() {
        double r = pointHandler.getPoint().getR();
        return r * (r / 2) + 0.5f * (r / 2) * (r / 2) + 0.25f * Math.PI * r * r;
    }
}

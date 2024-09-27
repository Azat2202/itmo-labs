package connector;

import org.projog.api.Projog;
import org.projog.api.QueryResult;

import java.io.File;

public class Connector {
    private static final Projog projog;

    static {
        projog = new Projog();
        projog.consultFile(new File("model.pl"));
    }

    public static QueryResult executeQuery(String query) {
        return projog.executeQuery(query);
    }
}
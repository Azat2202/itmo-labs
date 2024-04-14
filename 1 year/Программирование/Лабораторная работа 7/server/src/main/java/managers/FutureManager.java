package managers;

import main.App;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Класс хранящий коллекцию fixedThreadPool для их проверки на готовность и выполнения
 */
public class FutureManager {
    private static final Collection<Future<ConnectionManagerPool>> fixedThreadPoolFutures = new ArrayList<>();
    private static final Logger futureManagerLogger = LogManager.getLogger(FutureManager.class);

    public static void addNewFixedThreadPoolFuture(Future<ConnectionManagerPool> future){
        fixedThreadPoolFutures.add(future);
    }

    public static void checkAllFutures(){
        if(!fixedThreadPoolFutures.isEmpty()) {
            App.rootLogger.debug("------------------------СПИСОК ВСЕХ ПОТОКОВ---------------------------");
            fixedThreadPoolFutures.forEach(s -> futureManagerLogger.debug(s.toString()));
            App.rootLogger.debug("-------------------------------КОНЕЦ----------------------------------");
        }
        fixedThreadPoolFutures.stream()
                .filter(Future::isDone)
                .forEach(f -> {
                    try {
                        ConnectionManager.submitNewResponse(f.get());

                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                });
        fixedThreadPoolFutures.removeIf(Future::isDone);
    }
}

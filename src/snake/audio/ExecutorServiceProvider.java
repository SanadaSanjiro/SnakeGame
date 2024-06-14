package snake.audio;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceProvider {
    private static ExecutorService es = Executors.newCachedThreadPool();

    public static ExecutorService getExecutorService() {
        return es;
    }
}

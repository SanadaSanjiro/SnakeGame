package snake.audio;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Provides an Executor service that is used to play music files in separate threads.
public class ExecutorServiceProvider {
    private static ExecutorService es = Executors.newCachedThreadPool();

    public static ExecutorService getExecutorService() {
        return es;
    }
}

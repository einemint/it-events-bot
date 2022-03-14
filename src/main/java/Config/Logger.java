package Config;

import org.apache.logging.log4j.LogManager;

public class Logger {
    private static volatile org.apache.logging.log4j.Logger instance;

    public static org.apache.logging.log4j.Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = LogManager.getRootLogger();
                }
            }
        }
        return instance;
    }
}


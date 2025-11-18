package backend_api.utils;

import java.util.logging.Logger;

public class Log {
    private static final Logger logger = Logger.getLogger(Log.class.getName());

    private Log() {
        throw new IllegalArgumentException("Utility class");
    }

    public static void printLog(String message) {
        logger.info(message);
    }
}
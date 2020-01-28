package hotelicus.core;

import hotelicus.App;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class LoggerUtil {
    private static final Logger logger = Logger.getLogger(App.class);

    public static void init() {
        LogManager.getRootLogger().setLevel(Level.INFO);
    }

    public static void error (String error) {
        logger.error(error);
    }

    public static void info (String info) {
        logger.info(info);
    }

    public static void debug (String debug) {
        logger.info(debug);
    }

    public static void warn (String warn) {
        logger.warn(warn);
    }
}

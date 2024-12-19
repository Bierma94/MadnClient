package de.bierma.config;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class FileLogger {
    private static final Logger logger = Logger.getLogger(FileLogger.class.getName());
    private static FileLogger instance;

    private FileLogger() {
        // Remove the default console handler
        Logger rootLogger = Logger.getLogger("");
        ConsoleHandler consoleHandler = null;
        for (var handler : rootLogger.getHandlers()) {
            if (handler instanceof ConsoleHandler) {
                consoleHandler = (ConsoleHandler) handler;
                break;
            }
        }
        if (consoleHandler != null) {
            rootLogger.removeHandler(consoleHandler);
        }
    }

    public static FileLogger getInstance() {
        if (instance == null) {
            instance = new FileLogger();
        }
        return instance;
    }

    static {
        try {
            // Create a FileHandler that writes log messages to a file
            FileHandler fileHandler = new FileHandler("application.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.severe("Failed to initialize logger handler: " + e.getMessage());
        }
    }

    public void logInfo(String message) {
        logger.info(message);
    }

    public void logWarning(String message) {
        logger.warning(message);
    }

    public void logSevere(String message) {
        logger.severe(message);
    }
}
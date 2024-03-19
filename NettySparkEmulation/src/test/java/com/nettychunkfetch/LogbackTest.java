package com.nettychunkfetch;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.LoggerFactory;

public class LogbackTest {
    public static void main(String[] args) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            // Assuming logback.xml is directly under src/main/resources
            configurator.doConfigure(LogbackTest.class.getResourceAsStream("/logback.xml"));
        } catch (Exception e) {
            System.err.println("Error configuring Logback: " + e.getMessage());
        }

        StatusPrinter.printInCaseOfErrorsOrWarnings(context);

        // Add your logging test here
        LoggerFactory.getLogger(LogbackTest.class).debug("This is a test debug message");
    }
}

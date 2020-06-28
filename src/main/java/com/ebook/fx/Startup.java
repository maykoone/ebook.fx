package com.ebook.fx;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.stage.Stage;
import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

/**
 *
 * @author maykoone
 */
public class Startup extends Application {

    private SeContainer container;
    private static final Logger LOGGER = Logger.getLogger(Startup.class.getName(), "messages");

    @Override
    public void init() throws Exception {
        LOGGER.log(Level.INFO, "msg.application.init");
        /* Scan only the MainApp class package recursively */
        container = SeContainerInitializer.newInstance()
                        .disableDiscovery()
                        .addPackages(true, MainApp.class)
                        .initialize();
    }

    @Override
    public void start(Stage stage) throws Exception {
        LOGGER.log(Level.INFO, "msg.application.start");
        container.getBeanManager().fireEvent(stage);
    }

    @Override
    public void stop() throws Exception {
        LOGGER.log(Level.INFO, "msg.application.stop");
        container.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

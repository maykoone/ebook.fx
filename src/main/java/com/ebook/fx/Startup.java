/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebook.fx;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.stage.Stage;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 *
 * @author maykoone
 */
public class Startup extends Application {

    private Weld weld;
    private static final Logger LOGGER = Logger.getLogger(Startup.class.getName(), "messages");

    @Override
    public void init() throws Exception {
        LOGGER.log(Level.INFO, "msg.application.init");
        weld = new Weld().disableDiscovery()
                //                .packages(MainApp.class, MainController.class,
                //                        MainView.class, FXMLLoaderProducer.class, ImageCache.class)
                .addPackage(true, MainApp.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        LOGGER.log(Level.INFO, "msg.application.start");
        WeldContainer container = weld.initialize();
        container.event().select(Stage.class).fire(stage);
    }

    @Override
    public void stop() throws Exception {
        LOGGER.log(Level.INFO, "msg.application.stop");
//        ImageCache.getInstance().clear();
        weld.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

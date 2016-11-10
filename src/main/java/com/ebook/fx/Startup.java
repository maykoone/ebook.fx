/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebook.fx;

import com.ebook.fx.core.di.FXMLLoaderProducer;
import com.ebook.fx.core.util.ImageCache;
import com.ebook.fx.ui.controllers.MainController;
import com.ebook.fx.ui.views.MainView;
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

    @Override
    public void init() throws Exception {
        System.out.println("Initializing Weld...");
        weld = new Weld().disableDiscovery()
//                .packages(MainApp.class, MainController.class,
//                        MainView.class, FXMLLoaderProducer.class, ImageCache.class)
                .addPackage(true, MainApp.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("Initializing container...");
        WeldContainer container = weld.initialize();
        System.out.println("Firing start event...");
        container.event().select(Stage.class).fire(stage);
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Application stoping...");
//        ImageCache.getInstance().clear();
        weld.shutdown();
    }

    public static void main(String[] args) {
        System.out.println("Launching application...");
        launch(args);
    }
}

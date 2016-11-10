/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebook.fx.core.di;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

/**
 *
 * @author maykoone
 */
//@ApplicationScoped
public class FXMLLoaderProducer {

    @Inject
    private Instance<Object> instance;
    private static final String FXML_SUFFIX = ".fxml";

    @Produces
    public FXMLLoader produce(InjectionPoint injectionPoint, ResourceBundle resources) {
        System.out.println("FXML Loader producer");
        String viewName = injectionPoint.getBean().getBeanClass().getSimpleName();
        final URL fxmlPath = getClass().getResource("/fxml/" + viewName + FXML_SUFFIX);
        FXMLLoader fxmlLoader = resources == null ? new FXMLLoader(fxmlPath) : new FXMLLoader(fxmlPath, resources);
        fxmlLoader.setControllerFactory(controllerClass -> instance.select(controllerClass).get());
        return fxmlLoader;
    }

}

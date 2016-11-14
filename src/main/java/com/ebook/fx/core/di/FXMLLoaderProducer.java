package com.ebook.fx.core.di;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
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
        String viewName = injectionPoint.getBean().getBeanClass().getSimpleName();
        final URL fxmlPath = getClass().getResource("/fxml/" + viewName + FXML_SUFFIX);
        FXMLLoader fxmlLoader = resources == null ? new FXMLLoader(fxmlPath) : new FXMLLoader(fxmlPath, resources);
        fxmlLoader.setControllerFactory(controllerClass -> instance.select(controllerClass).get());
        return fxmlLoader;
    }

}

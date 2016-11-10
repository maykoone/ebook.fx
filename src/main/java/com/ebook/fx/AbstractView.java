package com.ebook.fx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author maykoone
 */
@ApplicationScoped
public class AbstractView {

    protected FXMLLoader fxmlLoader;
    private final String viewName;
    private static final String FXML_SUFFIX = ".fxml";
    private Stage currentStage;

    protected MainApp application;
    protected ResourceBundle bundle;

    public AbstractView(MainApp application) {
        this.viewName = this.getClass().getSimpleName();
        this.application = application;
    }

    private void loadFromFXML() {
        try {
            if (fxmlLoader == null) {
                bundle = application.getResourceBundle();
                final URL fxmlPath = getClass().getResource("/" + viewName + FXML_SUFFIX);
                this.fxmlLoader = bundle == null ? new FXMLLoader(fxmlPath) : new FXMLLoader(fxmlPath, bundle);
                fxmlLoader.load();

                initAbstractController();
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Can't load the view with name " + viewName, ex);
        }
    }

    protected Parent getView() {
        loadFromFXML();
        return fxmlLoader.getRoot();
    }

    public <T> T getController() {
        loadFromFXML();
        return fxmlLoader.<T>getController();
    }

    private void initAbstractController() {
        Object controller = fxmlLoader.getController();
        if (controller instanceof AbstractController) {
            ((AbstractController) controller).setView(this);
            ((AbstractController) controller).setApplication(application);
        }
    }

    public void show(Stage stage) {
        Scene scene = new Scene(this.getView());
        scene.getStylesheets().add(application.getDefaultStyle());

        if (bundle != null) {
            stage.setTitle(bundle.getString(viewName.toLowerCase() + ".title"));
        }
        stage.setScene(scene);
        stage.show();
    }

    public void show() {
        this.show(application.getPrimaryStage());
    }

    public MainApp getApplication() {
        return this.application;
    }

    protected String getViewName() {
        return viewName;
    }

//    Supplier<ResourceBundle> resourceSupplier = () -> ResourceBundle.getBundle(DEFAULT_RESOURCE_BUNDLE);
    public Stage getCurrentStage() {
        return currentStage != null ? currentStage : application.getPrimaryStage();
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }
}

package com.ebook.fx.ui.views;

import com.ebook.fx.ui.controllers.AbstractController;
import com.ebook.fx.MainApp;
import java.io.IOException;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.inject.Inject;

/**
 *
 * @author maykoone
 */
public abstract class AbstractView {

    private final String viewName;
    private static final String FXML_SUFFIX = ".fxml";
    
    protected FXMLLoader fxmlLoader;
    
    protected Stage stageView;
    
    protected ResourceBundle bundle;
    
    @Inject
    public AbstractView(FXMLLoader fxmlLoader, ResourceBundle bundle) {
        this.fxmlLoader = fxmlLoader;
        this.bundle = bundle;
        this.viewName = this.getClass().getSimpleName();        
    }

    protected Parent getView() {
        loadFromFXML();
        return fxmlLoader.getRoot();
    }

    public <T> T getController() {
        loadFromFXML();
        return fxmlLoader.<T>getController();
    }

    public void show(Stage stage) {
        this.stageView = Objects.requireNonNull(stage);
        Scene scene = new Scene(this.getView());
        scene.getStylesheets().add(MainApp.getDefaultStyle());
        scene.getStylesheets().add(MainApp.getDefaultFont());

        if (bundle != null) {
            stage.setTitle(bundle.getString(viewName.toLowerCase() + ".title"));
        }
        stage.setScene(scene);
        stage.show();
    }   

    protected String getViewName() {
        return viewName;
    }

    public Stage getStageView() {
        return stageView;
    }

    public void setStageView(Stage stageView) {
        this.stageView = stageView;
    }
    
    private void loadFromFXML() {
        try {
            if (isFxmlLoaded())
                return;
            
            fxmlLoader.load();
            initAbstractController();
        } catch (IOException ex) {
            throw new IllegalStateException("Can't load the view with name " + viewName, ex);
        }
    }

    private boolean isFxmlLoaded() {
        return fxmlLoader != null && fxmlLoader.getRoot() != null;
    }
    
    private void initAbstractController() {
        Object controller = fxmlLoader.getController();
        if (controller instanceof AbstractController) {
            ((AbstractController) controller).setView(this);            
        }
    }
}

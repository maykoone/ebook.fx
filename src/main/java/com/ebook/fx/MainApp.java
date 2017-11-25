package com.ebook.fx;

import com.ebook.fx.ui.views.MainView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class MainApp {
    
    private static final String DEFAULT_STYLE = "/styles/Styles.css";
    private static final String DEFAULT_FONT = "https://fonts.googleapis.com/css?family=Roboto+Condensed";

    @Inject
    private MainView view;
    
    private Stage primaryStage;
    
    public void start(@Observes Stage stage) throws Exception {
        this.primaryStage = stage;
        this.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        view.show(primaryStage);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static String getDefaultStyle() {
        return DEFAULT_STYLE;
    }
    
    public static String getDefaultFont() {
        return DEFAULT_FONT;
    }

}

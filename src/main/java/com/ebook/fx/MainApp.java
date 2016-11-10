package com.ebook.fx;

import com.ebook.fx.ui.views.MainView;
import javafx.stage.Stage;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class MainApp {

    private Stage primaryStage;
    @Inject
    private MainView view;
    private static final String DEFAULT_STYLE = "/styles/Styles.css";

    public void start(@Observes Stage stage) throws Exception {
        this.primaryStage = stage;

        view.show();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public String getDefaultStyle() {
        return DEFAULT_STYLE;
    }

}

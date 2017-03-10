package com.ebook.fx;

import com.ebook.fx.ui.views.MainView;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;
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
        this.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        view.show();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public String getDefaultStyle() {
        return DEFAULT_STYLE;
    }

    public Optional<ButtonType> showConfirmationDialog(final Window owner, final String title, final String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.setHeaderText(null);

        return alert.showAndWait();
    }
    
    public Optional<ButtonType> showConfirmationDialog(final String title, final String message) {
        return showConfirmationDialog(primaryStage, title, message);
    }

}

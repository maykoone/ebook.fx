package com.ebook.fx.ui.views;

import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author maykoone
 */
@ApplicationScoped
public class MainView extends AbstractView {
    
    @Inject
    public MainView(FXMLLoader fxmlLoader, ResourceBundle bundle) {
        super(fxmlLoader, bundle);
    }
    
    private Optional<ButtonType> showConfirmationDialog(final Window owner, final String title, final String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.setHeaderText(null);

        return alert.showAndWait();
    }
    
    public Optional<ButtonType> showConfirmationDialog(final String title, final String message) {
        return showConfirmationDialog(stageView, title, message);
    }
    
}

package com.ebook.fx.ui.views;

import com.ebook.fx.MainApp;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.inject.Inject;

/**
 *
 * @author maykoone
 */
public class BookEditView extends AbstractView {
    
    @Inject
    public BookEditView(FXMLLoader fxmlLoader, ResourceBundle bundle) {
        super(fxmlLoader, bundle);
    }

    @Override
    public void show(Stage stageOwner) {
        Scene scene = new Scene(getView());
        scene.getStylesheets().add(MainApp.getDefaultStyle());
        
        this.stageView = new Stage();
        if (bundle != null) {
            stageView.setTitle(bundle.getString(getViewName().toLowerCase() + ".title"));
        }
        stageView.initModality(Modality.WINDOW_MODAL);
        stageView.initOwner(stageOwner);
        stageView.setScene(scene);
        setStageView(stageView);
        stageView.showAndWait();
    }
    
}

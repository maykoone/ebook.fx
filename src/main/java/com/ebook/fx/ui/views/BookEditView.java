/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebook.fx.ui.views;

import com.ebook.fx.MainApp;
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
    public BookEditView(MainApp application) {
        super(application);
    }

    @Override
    public void show() {
        Scene scene = new Scene(getView());
        scene.getStylesheets().add(getApplication().getDefaultStyle());
        Stage stage = new Stage();
        if (bundle != null) {
            stage.setTitle(bundle.getString(getViewName().toLowerCase() + ".title"));
        }
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(getApplication().getPrimaryStage());
        stage.setScene(scene);
        setCurrentStage(stage);
        stage.showAndWait();
    }
    
}

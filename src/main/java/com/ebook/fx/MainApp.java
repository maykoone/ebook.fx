package com.ebook.fx;

import com.ebook.fx.model.Book;
import com.ebook.fx.util.ImageCache;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import static javafx.application.Application.launch;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class MainApp {

    private Stage primaryStage;
    @Inject
    private MainView view;

    public void start(@Observes Stage stage) throws Exception {
        System.out.println("started event catch");
        this.primaryStage = stage;

//        MainView view = new MainView(this);
        view.show();
//        MainController controller = view.getController();
//        controller.setApplication(this);
//
//        Scene scene = new Scene(view.getView());
//        scene.getStylesheets().add(DEFAULT_STYLE);
//
//        stage.setTitle("EbookFx");
//        stage.setScene(scene);
//        stage.show();
    }

//    @Override
//    public void stop() throws Exception {
//        ImageCache.getInstance().clear();
//    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
//    public static void main(String[] args) {
//        launch(args);
//    }

    public void showEditBookView(Book selectedBook) {
        BookEditView view = new BookEditView(this);

        Scene scene = new Scene(view.getView());
        scene.getStylesheets().add(DEFAULT_STYLE);

        Stage stage = new Stage();
        stage.setTitle("Edit Book");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        stage.setScene(scene);

        BookEditController controller = view.getController();
        controller.setBook(selectedBook);
//        controller.setView(stage);

        stage.showAndWait();
        System.out.println("book edit terminate.");
    }
    private static final String DEFAULT_STYLE = "/styles/Styles.css";

    private static final String DEFAULT_RESOURCE_BUNDLE = "messages";

    public ResourceBundle getResourceBundle() {
        try {
            return ResourceBundle.getBundle(DEFAULT_RESOURCE_BUNDLE);
        } catch (MissingResourceException e) {
            return null;
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public String getDefaultStyle() {
        return DEFAULT_STYLE;
    }


    static class BookEditView extends AbstractView {

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

}

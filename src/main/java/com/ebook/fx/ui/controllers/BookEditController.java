package com.ebook.fx.ui.controllers;

import com.ebook.fx.core.model.Book;
import com.ebook.fx.core.util.ImageCache;
import com.ebook.fx.ui.views.TagsBar;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javax.inject.Inject;

/**
 *
 * @author maykoone
 */
public class BookEditController extends AbstractController {

    @FXML
    private TagsBar tagsField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField publisherField;
    @FXML
    private ImageView bookCover;
    @Inject
    private ImageCache imageCache;

    private ObjectProperty<Book> currentBook;
    private ObjectProperty<Book> selectedBook;

    @FXML
    private void initialize() {
        this.currentBook = new SimpleObjectProperty<>();
        this.selectedBook = new SimpleObjectProperty<>();
        this.selectedBook.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setBook(newValue);
            }
        });
    }

    private void setBook(Book selectedBook) {
        titleField.setText(selectedBook.getTitle());
        authorField.setText(selectedBook.getAuthor());
        publisherField.setText(selectedBook.getPublisher());

        selectedBook.getTags().forEach(tag -> tagsField.addTag(tag));

        imageCache.getAsync(selectedBook.getFilePath()).thenAcceptAsync(bookCover::setImage);
    }

    @FXML
    private void handleOk(ActionEvent event) {
        Book editedBook = selectedBook.get();
        editedBook.setTitle(titleField.getText());
        editedBook.setAuthor(authorField.getText());
        editedBook.setPublisher(publisherField.getText());
        editedBook.setTags(tagsField.getTags());
        this.currentBook.set(editedBook);
        view.getStageView().close();
    }

    @FXML
    private void handleCancel() {
        view.getStageView().close();
    }

    public ObjectProperty<Book> currentBookProperty() {
        return currentBook;
    }

    public ObjectProperty<Book> selectedBookProperty() {
        return selectedBook;
    }

}

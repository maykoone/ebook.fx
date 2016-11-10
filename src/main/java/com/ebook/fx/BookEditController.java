/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebook.fx;

import com.ebook.fx.model.Book;
import com.ebook.fx.util.ImageCache;
import com.ebook.fx.view.TagsBar;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

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

    private ObjectProperty<Book> currentBook;
    private ObjectProperty<Book> selectedBook;

    @FXML
    private void initialize() {
        this.currentBook = new SimpleObjectProperty<>();
        this.selectedBook = new SimpleObjectProperty<>();
        this.selectedBook.addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                setBook(newValue);
            }
        });
    }

    void setBook(Book selectedBook) {
//        this.selectedBook.set(selectedBook);

        titleField.setText(selectedBook.getTitle());
        authorField.setText(selectedBook.getAuthor());
        publisherField.setText(selectedBook.getPublisher());

        selectedBook.getTags().forEach(tag -> tagsField.addTag(tag));

        ImageCache.getInstance().getAsync(selectedBook.getFilePath()).thenAcceptAsync(bookCover::setImage);
    }

    @FXML
    private void handleOk(ActionEvent event) {
        Book editedBook = selectedBook.get();
        editedBook.setTitle(titleField.getText());
        editedBook.setAuthor(authorField.getText());
        editedBook.setPublisher(publisherField.getText());
        editedBook.setTags(tagsField.getTags());
        currentBook.set(editedBook);
        view.getCurrentStage().close();
    }

    @FXML
    private void handleCancel() {
        view.getCurrentStage().close();
    }

    public ObjectProperty<Book> currentBookProperty() {
        return currentBook;
    }

    public ObjectProperty<Book> selectedBookProperty() {
        return selectedBook;
    }

}

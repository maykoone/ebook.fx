/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebook.fx;

import com.ebook.fx.command.LoadPdfCoverImageCommand;
import com.ebook.fx.model.Book;
import com.ebook.fx.task.ImportFileTask;
import com.ebook.fx.util.ImageCache;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

/**
 *
 * @author maykoone
 */
public class MainController extends AbstractController {

    @FXML
    private TableView<Book> booksTable;
    private ObservableList<Book> books;
    @FXML
    private ListView<String> navigationList;
    @FXML
    private TextField searchBox;
    @FXML
    private ListView<String> bookIndexList;
    @FXML
    private ImageView bookCover;
    @FXML
    private ProgressBar progressBar;

    public MainController() {
        books = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        navigationList.setItems(FXCollections.observableArrayList("Library",
                "Favorites", "Recents"));
//        bookIndexList.setItems(FXCollections.observableArrayList("I.Introduction",
//                "Chapter 2", "Chapter 3", "Chapter 4", "Chapter 5"));

        progressBar.setVisible(false);

        MenuItem mEdit = new MenuItem("Edit");
        mEdit.setOnAction(e -> {
            System.out.println("edit menu clicked " + booksTable.getSelectionModel().getSelectedItem().getTitle());
            editBookAction();
        });
        MenuItem mOpen = new MenuItem("Open");
        mOpen.setOnAction(e -> {
            EventQueue.invokeLater(() -> {
                try {
                    Desktop.getDesktop().open(new File(booksTable.getSelectionModel().getSelectedItem().getFilePath()));
                } catch (IOException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, "Open file fail.", ex);
                }
            });
        });
        ContextMenu tableMenu = new ContextMenu(mEdit, mOpen);
        booksTable.setContextMenu(tableMenu);

        booksTable.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Book> observable, Book oldValue, Book newValue) -> {
            if (newValue != null) {
                if (newValue.getFilePath() != null) {
//                    Task<Image> loadImageTask = ImageCache.getInstance().get(newValue.getFilePath());
//                    loadImageTask.setOnSucceeded(e -> bookCover.setImage(loadImageTask.getValue()));

                    ImageCache.getInstance().getAsync(newValue.getFilePath()).thenAcceptAsync(bookCover::setImage);
                } else {
                    bookCover.setImage(LoadPdfCoverImageCommand.defaultImage);
                }
                bookIndexList.setItems(FXCollections.observableArrayList(newValue.getContents()));
            }
        });
    }

    @FXML
    private void importFolder(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File directory = chooser.showDialog(getApplication().getPrimaryStage());

        if (directory != null) {
            System.out.println("setting directory to : " + directory.getAbsolutePath());
            //only pdf
            ImportFileTask importTask = new ImportFileTask(directory);

            Alert importInfo = new Alert(Alert.AlertType.CONFIRMATION);
            importInfo.setTitle("Import folder...");
            importInfo.setContentText("There's " + importTask.getNumberOfFiles() + " to be imported. Do you want to continue?");
            Optional<ButtonType> option = importInfo.showAndWait();
            if (option.get().equals(ButtonType.OK)) {
                System.out.println("Perform import");
                importTask.setOnSucceeded(e -> {
                    booksTable.getItems().addAll(importTask.getValue());
                    hideProgressBar();
                });
                showProgressBar(importTask);
                new Thread(importTask).start();
            } else {
                System.out.println("do nothing");
            }
        }

    }

    @FXML
    private void importFile(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf", "*.PDF"));
        File file = chooser.showOpenDialog(getApplication().getPrimaryStage());

        if (file != null) {
            ImportFileTask task = new ImportFileTask(file);
            task.setOnSucceeded(e -> {
                booksTable.getItems().addAll(task.getValue());
                hideProgressBar();
            });
            showProgressBar(task);
            new Thread(task).start();
        }
    }

    @FXML
    private void aboutAction(ActionEvent event) {
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.setTitle("About");
        about.setContentText("This is a about window!");
        about.showAndWait();

    }

    @FXML
    private void editBookAction() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            MainApp.BookEditView bookEditView = new MainApp.BookEditView(getApplication());
            BookEditController bookEditController = bookEditView.getController();
//            bookEditController.setBook(selectedBook);
            bookEditController.selectedBookProperty().bind(booksTable.getSelectionModel().selectedItemProperty());
            bookEditController.currentBookProperty().addListener((observable, oldBook, newBook) -> {
                if (newBook != null) {
                    booksTable.refresh();
                }
            });
            bookEditView.show();
        }
    }

    private void showProgressBar(Worker<?> task) {
        progressBar.progressProperty().bind(task.progressProperty());
        progressBar.setVisible(true);
    }

    private void hideProgressBar() {
        progressBar.setVisible(false);
        progressBar.progressProperty().unbind();
        progressBar.setProgress(0);
    }

    public void setApplication(MainApp application) {
        this.application = application;
    }

}

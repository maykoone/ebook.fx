package com.ebook.fx.ui.controllers;

import com.ebook.fx.MainApp;
import com.ebook.fx.ui.views.BookEditView;
import com.ebook.fx.core.command.LoadPdfCoverImageCommand;
import com.ebook.fx.core.model.Book;
import com.ebook.fx.core.services.BookRepository;
import com.ebook.fx.core.task.ImportFileTask;
import com.ebook.fx.core.util.ImageCache;
import com.ebook.fx.preferences.PreferencesUtil;
import com.ebook.fx.ui.views.MainView;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
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
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 *
 * @author maykoone
 */
public class MainController {

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
    @Inject
    private Instance<BookEditView> bookEditViewSource;
    @Inject
    private MainView view;
    @Inject
    private MainApp application;
    @Inject
    private ImageCache imageCache;
    @FXML
    private ResourceBundle resources;
    @Inject
    private Logger logger;
    @Inject
    private BookRepository repository;
    @Inject
    private PreferencesUtil prefs;

    @FXML
    private void initialize() {
        books = FXCollections.observableArrayList();

        progressBar.setVisible(false);
        initBooksTable();
        this.books.addAll(repository.list());

        //setup search
        booksTable.setItems(filterAndSortBooks(Predicates.forMenuLibrary()));
        this.searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            booksTable.setItems(filterAndSortBooks(Predicates.forSearch(newValue)));
        });

        //Navigation List
        navigationList.setItems(FXCollections.observableArrayList(resources.getString("menu.library"),
                resources.getString("menu.favorites")));
        navigationList.getSelectionModel().select(resources.getString("menu.library"));
        navigationList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (resources.getString("menu.library").equals(newValue)) {
                booksTable.setItems(filterAndSortBooks(Predicates.forMenuLibrary().and(Predicates.forSearch(searchBox.getText()))));
            } else if (resources.getString("menu.favorites").equals(newValue)) {
                booksTable.setItems(filterAndSortBooks(Predicates.forMenuFavorites()));
            }
        });

    }

    private SortedList<Book> filterAndSortBooks(Predicate<Book> predicate) {
        SortedList<Book> sorted = books.filtered(predicate).sorted();
        sorted.comparatorProperty().bind(booksTable.comparatorProperty());
        return sorted;
    }

    private void initBooksTable() {
        MenuItem mEdit = new MenuItem(resources.getString("label.edit"));
        mEdit.setOnAction(e -> {
            editBookAction();
        });

        MenuItem mOpen = new MenuItem(resources.getString("label.open"));
        mOpen.setOnAction(e -> {
            EventQueue.invokeLater(() -> {
                try {
                    Desktop.getDesktop().open(new File(booksTable.getSelectionModel().getSelectedItem().getFilePath()));
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "Open file fail.", ex);
                }
            });
        });
        MenuItem mRemove = new MenuItem(resources.getString("label.remove"));
        mRemove.setOnAction(e -> {
            removeBookAction();
        });

        MenuItem mAddFavorite = new MenuItem(resources.getString("label.addfavorite"));
        mAddFavorite.setOnAction(e -> toggleFavoriteBookAction());

        ContextMenu tableMenu = new ContextMenu(mEdit, mOpen, mRemove, mAddFavorite);
        booksTable.setContextMenu(tableMenu);

        booksTable.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Book> observable, Book oldValue, Book newValue) -> {
            if (newValue != null) {
                if (newValue.getFilePath() != null) {
                    imageCache.getAsync(newValue.getFilePath())
                            .exceptionally(ex -> {
                                logger.info("Fail to load image. Returning default.");
                                return LoadPdfCoverImageCommand.defaultImage;
                            })
                            .thenAcceptAsync(bookCover::setImage);
                } else {
                    bookCover.setImage(LoadPdfCoverImageCommand.defaultImage);
                }
                bookIndexList.setItems(FXCollections.observableArrayList(newValue.getContents()));
                mAddFavorite.setText(newValue.isFavorite() ? resources.getString("label.removefavorite") : resources.getString("label.addfavorite"));
            }
        });
    }

    /*
    *   ACTIONS /////////////////////
     */
    @FXML
    private void importFolder(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(new File(prefs.get(PreferencesUtil.LAST_OPEN_DIRECTORY_PREF, System.getProperty("user.home"))));
        File directory = chooser.showDialog(application.getPrimaryStage());

        if (directory != null) {
            prefs.set(PreferencesUtil.LAST_OPEN_DIRECTORY_PREF, directory.getAbsolutePath());
            //only pdf
            ImportFileTask importTask = new ImportFileTask(directory, book -> repository.save(book));

            Optional<ButtonType> option = application.showConfirmationDialog(resources.getString("dialog.import.folder.title"),
                    MessageFormat.format(resources.getString("dialog.import.folder.msg"), importTask.getNumberOfFiles()));
            option.filter(button -> button.equals(ButtonType.OK)).ifPresent(button -> {
                logger.log(Level.INFO, "Perform import");
                importTask.setOnSucceeded(e -> {
                    books.addAll(importTask.getValue());
                    hideProgressBar();
                });
                showProgressBar(importTask);
                new Thread(importTask).start();
            });
        }

    }

    @FXML
    private void importFile(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(prefs.get(PreferencesUtil.LAST_OPEN_DIRECTORY_PREF, System.getProperty("user.home"))));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf", "*.PDF"));
        List<File> files = chooser.showOpenMultipleDialog(application.getPrimaryStage());

        if (files != null && !files.isEmpty()) {
            prefs.set(PreferencesUtil.LAST_OPEN_DIRECTORY_PREF, files.get(0).getParent());
            ImportFileTask task = new ImportFileTask(files, book -> repository.save(book));
            task.setOnSucceeded(e -> {
                books.addAll(task.getValue());
                hideProgressBar();
            });
            showProgressBar(task);
            new Thread(task).start();
        }
    }

    @FXML
    private void aboutAction(ActionEvent event) {
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.setTitle(resources.getString("dialog.about.title"));
        about.setContentText(resources.getString("dialog.about.msg"));
        about.showAndWait();
    }

    @FXML
    private void editBookAction() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            BookEditView bookEditView = bookEditViewSource.get();
            BookEditController bookEditController = bookEditView.getController();
            bookEditController.selectedBookProperty().bind(booksTable.getSelectionModel().selectedItemProperty());
            bookEditController.currentBookProperty().addListener((observable, oldBook, newBook) -> {
                if (newBook != null) {
                    repository.save(newBook);
                    booksTable.refresh();
                }
            });
            bookEditView.show();
        }
    }

    private void showProgressBar(Worker<?> task) {
        progressBar.progressProperty().bind(task.progressProperty());
        progressBar.visibleProperty().bind(task.runningProperty());
//        progressBar.setVisible(true);
    }

    private void hideProgressBar() {
//        progressBar.setVisible(false);
        progressBar.progressProperty().unbind();
        progressBar.setProgress(0);
    }

    public void setApplication(MainApp application) {
        this.application = application;
    }

    private void removeBookAction() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            Optional<ButtonType> confirmation = application.showConfirmationDialog(resources.getString("dialog.remove.book.title"),
                    MessageFormat.format(resources.getString("dialog.remove.book.msg"), selectedBook.getTitle()));

            confirmation.filter(button -> button.equals(ButtonType.OK)).ifPresent(button -> {
                CompletableFuture.runAsync(() -> repository.remove(selectedBook))
                        .thenAcceptAsync(v -> {
                            books.remove(selectedBook);
                            booksTable.refresh();
                        }, Platform::runLater);
            });

        }
    }

    private void toggleFavoriteBookAction() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            selectedBook.setFavorite(!selectedBook.isFavorite());
            CompletableFuture.runAsync(() -> repository.save(selectedBook))
                    .thenAcceptAsync(v -> booksTable.refresh(), Platform::runLater);

        }
    }

}

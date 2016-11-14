package com.ebook.fx.core.task;

import com.ebook.fx.core.model.Book;
import com.ebook.fx.core.services.BookRepository;
import java.io.File;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javax.inject.Inject;

/**
 *
 * @author maykoone
 */
public class BookImporterService extends Service<ObservableList<Book>>{
    private ObjectProperty<File> sourceFile = new SimpleObjectProperty<File>(this, "sourceFile");
    public void setSourceFile(File sourceFile) { this.sourceFile.set(sourceFile);}
    public File getSourceFile(){return this.sourceFile.get();}
    @Inject
    private BookRepository repository;

    @Override
    protected Task<ObservableList<Book>> createTask() {
        return new ImportFileTask(getSourceFile(), book -> repository.save(book));
    }
    
}

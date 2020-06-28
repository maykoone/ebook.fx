package com.ebook.fx.core.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author maykoone
 */
@Entity
@Table(name = "book")
public class Book implements Serializable {

    private StringProperty id;
    private StringProperty title;
    private StringProperty author;
    private IntegerProperty pages;
    private ListProperty<String> tags;
    private StringProperty publisher;
    private ObjectProperty<LocalDate> added;
    private StringProperty filePath;
    private DoubleProperty fileLength;
    private ListProperty<String> contents;
    private BooleanProperty favorite;

    public Book() {
        this.id = new SimpleStringProperty(UUID.randomUUID().toString());
        this.title = new SimpleStringProperty();
        this.author = new SimpleStringProperty();
        this.pages = new SimpleIntegerProperty(0);
        this.publisher = new SimpleStringProperty("Unknown");
        this.added = new SimpleObjectProperty<>(LocalDate.now());
        this.tags = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.filePath = new SimpleStringProperty();
        this.fileLength = new SimpleDoubleProperty();
        this.contents = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.favorite = new SimpleBooleanProperty(false);
    }

    public Book(String title, String author) {
        this();
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
    }

    @Id
    public String getId() {
        return this.id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getTitle() {
        return this.title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getAuthor() {
        return this.author.get();
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public Integer getPages() {
        return this.pages.get();
    }

    public void setPages(Integer pages) {
        this.pages.set(pages);
    }

    @ElementCollection
    public List<String> getTags() {
        return this.tags.get();
    }

    public void setTags(List<String> tags) {
        this.tags.set(FXCollections.observableArrayList(tags));
    }

    public String getPublisher() {
        return this.publisher.get();
    }

    public void setPublisher(String publisher) {
        this.publisher.set(publisher);
    }

    public LocalDate getAdded() {
        return this.added.get();
    }

    public void setAdded(LocalDate added) {
        this.added.set(added);
    }

    public String getFilePath() {
        return this.filePath.get();
    }

    public void setFilePath(String filePath) {
        this.filePath.set(filePath);
    }

    public Double getFileLength() {
        return this.fileLength.get();
    }

    public void setFileLength(Double fileLength) {
        this.fileLength.set(fileLength);
    }

    @ElementCollection
    public List<String> getContents() {
        return this.contents.get();
    }

    public void setContents(List<String> contents) {
        this.contents.set(FXCollections.observableArrayList(contents));
    }

    public BooleanProperty favoriteProperty() {
        return favorite;
    }

    public boolean isFavorite() {
        return this.favorite.get();
    }

    public void setFavorite(boolean favorite) {
        this.favorite.set(favorite);
    }

    public void parseTags(String keywords) {
        List<String> tagsList = new ArrayList<>();
        if (keywords != null && !keywords.isEmpty()) {
            tagsList = Stream.of(keywords.split(",")).map(t -> t.trim()).collect(Collectors.toList());
        }
        setTags(tagsList);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Book other = (Book) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Book{" + "id=" + id.get() + ", title=" + title.get() + ", author=" + author.get() + '}';
    }

}
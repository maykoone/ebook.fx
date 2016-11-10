/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebook.fx.core.model;

import java.time.LocalDate;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

/**
 *
 * @author maykoone
 */
public class BookModel implements Book {

    private StringProperty title;
    private StringProperty author;
    private IntegerProperty pages;
    private ListProperty<String> tags;
    private StringProperty publisher;
    private ObjectProperty<LocalDate> added;
    private StringProperty filePath;
    private DoubleProperty fileLength;
    private ListProperty<String> contents;

    public BookModel(String title, String author) {
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.pages = new SimpleIntegerProperty(0);
        this.publisher = new SimpleStringProperty("Unknown");
        this.added = new SimpleObjectProperty<>(LocalDate.now());
        this.tags = new SimpleListProperty<>(FXCollections.observableArrayList());
        this.filePath = new SimpleStringProperty();
        this.fileLength = new SimpleDoubleProperty();
        this.contents = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    @Override
    public String getTitle() {
        return this.title.get();
    }

    @Override
    public void setTitle(String title) {
        this.title.set(title);
    }

    @Override
    public String getAuthor() {
        return this.author.get();
    }

    @Override
    public void setAuthor(String author) {
        this.author.set(author);
    }

    @Override
    public Integer getPages() {
        return this.pages.get();
    }

    @Override
    public void setPages(Integer pages) {
        this.pages.set(pages);
    }

    @Override
    public List<String> getTags() {
        return this.tags.get();
    }

    @Override
    public void setTags(List<String> tags) {
        if (tags != null) {
            this.tags.set(FXCollections.observableArrayList(tags));
        }
    }

    @Override
    public String getPublisher() {
        return this.publisher.get();
    }

    @Override
    public void setPublisher(String publisher) {
        this.publisher.set(publisher);
    }

    @Override
    public LocalDate getAdded() {
        return this.added.get();
    }

    @Override
    public void setAdded(LocalDate added) {
        this.added.set(added);
    }

    @Override
    public String getFilePath() {
        return this.filePath.get();
    }

    @Override
    public void setFilePath(String filePath) {
        this.filePath.set(filePath);
    }

    @Override
    public Double getFileLength() {
        return this.fileLength.get();
    }

    @Override
    public void setFileLength(Double fileLength) {
        this.fileLength.set(fileLength);
    }

    @Override
    public List<String> getContents() {
        return this.contents.get();
    }

    @Override
    public void setContents(List<String> contents) {
        this.contents.set(FXCollections.observableArrayList(contents));
    }

}

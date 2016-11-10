package com.ebook.fx.model;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author maykoone
 */
public interface Book {

    String getTitle();
    void setTitle(String title);

    String getAuthor();
    void setAuthor(String author);

    Integer getPages();
    void setPages(Integer pages);

    List<String> getTags();
    void setTags(List<String> tags);

    String getPublisher();
    void setPublisher(String publisher);

    LocalDate getAdded();
    void setAdded(LocalDate added);
    
    String getFilePath();
    void setFilePath(String filePath);
    
    Double getFileLength();
    void setFileLength(Double fileLength);
    
    List<String> getContents();
    void setContents(List<String> contents);
}

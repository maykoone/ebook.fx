package com.ebook.fx.ui.controllers;

import com.ebook.fx.core.model.Book;
import java.util.function.Predicate;

/**
 *
 * @author maykoone
 */
public class Predicates {

    public static Predicate<Book> forSearch(String term) {
        return (book) -> {
            if (term == null || term.isEmpty()) {
                return true;
            } else {
                return book.getTitle().toLowerCase().contains(term.toLowerCase())
                        || book.getTags().stream().anyMatch(tag -> tag.toLowerCase().contains(term.toLowerCase()));
            }
        };
    }
    
    public static Predicate<Book> forMenuLibrary(){
        return (book) -> {
            return true;
        };
    }
    
    public static Predicate<Book> forMenuFavorites(){
        return (book) -> {
            return book.isFavorite();
        };
    }
}

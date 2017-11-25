package com.ebook.fx.ui.controllers;

import com.ebook.fx.core.model.Book;
import java.util.function.Predicate;

/**
 *
 * @author maykoone
 */
public class Predicates {
    
    private Predicates() {}

    public static Predicate<Book> forSearch(String term) {
        return book ->  (term == null || term.isEmpty()) ||
                        book.getTitle().toLowerCase().contains(term.toLowerCase()) || 
                        book.getTags().stream().anyMatch(tag -> tag.toLowerCase().contains(term.toLowerCase()));
    }
    
    public static Predicate<Book> forMenuLibrary(){
        return book -> true;
    }
    
    public static Predicate<Book> forMenuFavorites(){
        return book -> book.isFavorite();
    }
}

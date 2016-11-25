package com.ebook.fx.ui.views;

import com.ebook.fx.MainApp;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author maykoone
 */
@ApplicationScoped
public class MainView extends AbstractView {
    
    @Inject
    public MainView(MainApp application) {
        super(application);
    }
    
}

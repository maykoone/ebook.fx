/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

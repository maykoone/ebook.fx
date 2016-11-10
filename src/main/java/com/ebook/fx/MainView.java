/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebook.fx;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author maykoone
 */
//@ApplicationScoped
class MainView extends AbstractView {
    
    @Inject
    public MainView(MainApp application) {
        super(application);
    }
    
}

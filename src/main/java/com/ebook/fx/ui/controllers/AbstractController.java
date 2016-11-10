/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebook.fx.ui.controllers;

import com.ebook.fx.MainApp;
import com.ebook.fx.ui.views.AbstractView;

/**
 *
 * @author maykoone
 */
public abstract class AbstractController {

    protected MainApp application;
    protected AbstractView view;

    public MainApp getApplication() {
        return application;
    }

    public void setApplication(MainApp application) {
        this.application = application;
    }

    public AbstractView getView() {
        return view;
    }

    public void setView(AbstractView view) {
        this.view = view;
    }

}

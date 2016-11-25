package com.ebook.fx.core.di;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 *
 * @author maykoone
 */
public class ResourceBundleProducer {

    private static final String DEFAULT_RESOURCE_BUNDLE = "messages";

    @Produces
//    @ApplicationScoped
    public ResourceBundle produce() {
        try {
            return ResourceBundle.getBundle(DEFAULT_RESOURCE_BUNDLE);
        } catch (MissingResourceException e) {
            return null;
        }
    }
}

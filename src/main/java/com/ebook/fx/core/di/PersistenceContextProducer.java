package com.ebook.fx.core.di;

import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author maykoone
 */
public class PersistenceContextProducer {

    @Produces
    @ApplicationScoped
    public EntityManagerFactory produceFactory() {
        return Persistence.createEntityManagerFactory("com.ebook.fx.hibernate");
    }

    public void closeFactory(@Disposes EntityManagerFactory emf, Logger logger) {
        logger.info("Destroy EntityManagerFactory");
        if (emf.isOpen()) {
            emf.close();
        }
    }

    @Produces
    public EntityManager produceEntityManager(EntityManagerFactory emf) {
        return emf.createEntityManager();
    }

}

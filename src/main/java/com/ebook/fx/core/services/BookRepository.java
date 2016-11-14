package com.ebook.fx.core.services;

import com.ebook.fx.core.model.Book;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author maykoone
 */
public class BookRepository implements Closeable{

    private EntityManager entityManager;
    private EntityManagerFactory emf;
    private EntityTransaction transaction;
    @Inject private Logger logger;

    @PostConstruct
    public void init() {
        logger.log(Level.INFO, "initializing repository. Create EntityManagerFactory, EntityManager and Transaction");
        this.emf = Persistence.createEntityManagerFactory("com.ebook.fx");
        this.entityManager = emf.createEntityManager();
        this.transaction = entityManager.getTransaction();
    }

    public Book save(Book book) {
        logger.log(Level.INFO, "save book");
        this.transaction.begin();
        Book merged = entityManager.merge(book);
        transaction.commit();
        return merged;
    }

    public List<Book> list() {
        CriteriaQuery<Book> cq = entityManager.getCriteriaBuilder().createQuery(Book.class);
        return entityManager.createQuery(cq.select(cq.from(Book.class))).getResultList();
    }

    @Override
    public void close() throws IOException {
        this.transaction.begin();
        this.transaction.commit();
        this.entityManager.close();
    }
}

package com.ebook.fx.core.services;

import com.ebook.fx.core.model.Book;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author maykoone
 */
public class BookRepository {

    private EntityManager entityManager;
    private EntityTransaction transaction;
    @Inject
    private Logger logger;

    @Inject
    public BookRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostConstruct
    public void init() {
        logger.log(Level.INFO, "initializing repository. Create EntityManagerFactory, EntityManager and Transaction");
        this.transaction = entityManager.getTransaction();
    }

    public Book save(Book book) {
        this.transaction.begin();
        if (entityManager.contains(book)) {
            logger.log(Level.INFO, "merge book");
            book = entityManager.merge(book);
        } else {
            logger.log(Level.INFO, "persist book");
            entityManager.persist(book);
        }
        transaction.commit();
        return book;
    }
    
    public void remove(Book book){
        this.transaction.begin();
        logger.info("remove " + book);
        entityManager.remove(book);
        this.transaction.commit();
    }

    public List<Book> list() {
        CriteriaQuery<Book> cq = entityManager.getCriteriaBuilder().createQuery(Book.class);
        return entityManager.createQuery(cq.select(cq.from(Book.class))).getResultList();
    }

}

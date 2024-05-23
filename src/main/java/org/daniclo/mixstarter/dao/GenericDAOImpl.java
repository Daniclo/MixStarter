package org.daniclo.mixstarter.dao;

import org.daniclo.mixstarter.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GenericDAOImpl<T> implements GenericDAO<T> {

    private final Class<T> entityClass;

    public GenericDAOImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public Optional<T> findById(Long id) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Optional<T>> value = service.submit(()->{
            try (Session session = HibernateUtil.getSessionFactory().openSession();) {
                return Optional.ofNullable(session.find(entityClass, id));
            }
        });
        service.shutdown();
        try {
            return value.get();
        } catch (InterruptedException | ExecutionException e) {
            System.err.println(e.getLocalizedMessage());
        }
        return Optional.empty();
    }

    @Override
    public void create(T entity) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(()->{
            try (Session session = HibernateUtil.getSessionFactory().openSession();) {
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();
                    session.persist(entity);
                    tx.commit();
                } catch (RuntimeException e) {
                    if(tx != null)
                        tx.rollback();
                    System.err.println(e.getLocalizedMessage());
                }
            }
        });
        service.shutdown();
    }

    @Override
    public void save(T entity) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(()->{
            try (Session session = HibernateUtil.getSessionFactory().openSession();) {
                session.beginTransaction();
                session.merge(entity);
                session.getTransaction().commit();
            }
        });
        service.shutdown();
    }

    @Override
    public void deleteById(Long id) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(()->{
            try (Session session = HibernateUtil.getSessionFactory().openSession();) {
                session.beginTransaction();
                session.remove(session.find(entityClass, id));
                session.getTransaction().commit();
            }
        });
        service.shutdown();
    }

    @Override
    public void delete(T entity) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(()->{
            try (Session session = HibernateUtil.getSessionFactory().openSession();) {
                session.beginTransaction();
                session.remove(entity);
                session.getTransaction().commit();
            }
        });
        service.shutdown();
    }
}
package org.daniclo.mixstarter.dao;

import org.daniclo.mixstarter.model.Album;
import org.daniclo.mixstarter.model.Post;
import org.daniclo.mixstarter.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PostDAOImpl extends GenericDAOImpl<Post> implements PostDAO{
    public PostDAOImpl(Class<Post> entityClass) {
        super(entityClass);
    }

    @Override
    public List<Post> getPosts() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<List<Post>> value = service.submit(()->{
            try (Session session = HibernateUtil.getSessionFactory().openSession();){
                Query<Post> query = session.createQuery("from Post", Post.class);
                return query.getResultList();
            }
        });
        service.shutdown();
        try {
            return value.get();
        } catch (InterruptedException | ExecutionException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Post> getPostsByUser(String username) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<List<Post>> value = service.submit(()->{
            try (Session session = HibernateUtil.getSessionFactory().openSession();){
                Query<Post> query = session.createQuery("from Post where user.username = :username", Post.class)
                        .setParameter("username",username);
                return query.getResultList();
            }
        });
        service.shutdown();
        try {
            return value.get();
        } catch (InterruptedException | ExecutionException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

}

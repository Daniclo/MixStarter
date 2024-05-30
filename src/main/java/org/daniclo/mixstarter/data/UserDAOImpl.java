package org.daniclo.mixstarter.data;

import org.daniclo.mixstarter.model.Followers;
import org.daniclo.mixstarter.model.User;
import org.daniclo.mixstarter.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UserDAOImpl extends GenericDAOImpl<User> implements UserDAO{
    public UserDAOImpl(Class<User> entityClass) {
        super(entityClass);
    }

    @Override
    public List<User> getUsers() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<List<User>> value = service.submit(()->{
            try (Session session = HibernateUtil.getSessionFactory().openSession();){
                Query<User> query = session.createQuery("from User", User.class);
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
    public User getByName(String name) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<User> value = service.submit(()->{
            try (Session session = HibernateUtil.getSessionFactory().openSession();) {
                Query<User> query = session.createQuery("from User where username = :username", User.class)
                        .setParameter("username", name);
                return query.getSingleResultOrNull();
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
    public List<User> getFollowers(Long id) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<List<User>> value = service.submit(()->{
            try (Session session = HibernateUtil.getSessionFactory().openSession();){
                Query<Followers> query = session.createQuery("from Followers where followed.id = :userID", Followers.class)
                        .setParameter("userID",id);
                var followers = query.getResultList();
                List<User> followerList = new ArrayList<>();
                for (Followers follower:followers){
                    if (findById(follower.getFollows().getId()).isPresent())
                        followerList.add(findById(follower.getFollows().getId()).get());
                }
                return followerList;
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
    public List<User> getFollowing(Long id) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<List<User>> value = service.submit(()->{
            try (Session session = HibernateUtil.getSessionFactory().openSession();){
                Query<Followers> query = session.createQuery("from Followers where follows.id = :userID", Followers.class)
                        .setParameter("userID",id);
                var following = query.getResultList();
                List<User> followerList = new ArrayList<>();
                for (Followers follower:following){
                    if (findById(follower.getFollowed().getId()).isPresent())
                        followerList.add(findById(follower.getFollowed().getId()).get());
                }
                return followerList;
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

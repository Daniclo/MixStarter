package org.daniclo.mixstarter.dao;

import org.daniclo.mixstarter.model.*;
import org.daniclo.mixstarter.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AlbumDAOImpl extends GenericDAOImpl<Album> implements AlbumDAO {
    public AlbumDAOImpl(Class<Album> entityClass) {
        super(entityClass);
    }

    @Override
    public List<Album> getAlbums() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<List<Album>> value = service.submit(()->{
            try (Session session = HibernateUtil.getSessionFactory().openSession();){
                Query<Album> query = session.createQuery("from Album", Album.class);
                return query.getResultList();
            }
        });
        service.shutdown();
        try {
            return value.get();
        } catch (InterruptedException | ExecutionException e) {
            System.err.println(e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public List<Album> getAlbumsLikedByUser(User user) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<List<Album>> value = service.submit(()->{
            try (Session session = HibernateUtil.getSessionFactory().openSession();){
                Query<UserLikesAlbum> query = session.createQuery("from UserLikesAlbum where user.id = :userID", UserLikesAlbum.class)
                        .setParameter("userID",user.getId());
                var likedAlbums = query.getResultList();
                List<Album> returnList = new ArrayList<>();
                for (UserLikesAlbum u:likedAlbums){
                    returnList.add(u.getAlbum());
                }
                return returnList;
            }
        });
        service.shutdown();
        try {
            return value.get();
        } catch (InterruptedException | ExecutionException e) {
            System.err.println(e.getLocalizedMessage());
        }
        return null;
    }
}
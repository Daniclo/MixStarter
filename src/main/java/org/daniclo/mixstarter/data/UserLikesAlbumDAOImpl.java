package org.daniclo.mixstarter.data;

import org.daniclo.mixstarter.model.Album;
import org.daniclo.mixstarter.model.User;
import org.daniclo.mixstarter.model.UserLikesAlbum;
import org.daniclo.mixstarter.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UserLikesAlbumDAOImpl extends GenericDAOImpl<UserLikesAlbum> implements UserLikesAlbumDAO {

    public UserLikesAlbumDAOImpl(Class<UserLikesAlbum> entityClass) {
        super(entityClass);
    }

    @Override
    public UserLikesAlbum findByUserAndAlbum(User user, Album album) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<UserLikesAlbum> value = service.submit(()->{
            try (Session session = HibernateUtil.getSessionFactory().openSession()){
                Query<UserLikesAlbum> query = session.createQuery("from UserLikesAlbum where user.id = :userID and album.id = :albumID",
                                UserLikesAlbum.class)
                        .setParameter("userID",user.getId())
                        .setParameter("albumID",album.getId());
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
}
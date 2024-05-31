package org.daniclo.mixstarter.data;

import org.daniclo.mixstarter.model.Song;
import org.daniclo.mixstarter.model.User;
import org.daniclo.mixstarter.model.UserLikesAlbum;
import org.daniclo.mixstarter.model.UserLikesSong;
import org.daniclo.mixstarter.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UserLikesSongDAOImpl extends GenericDAOImpl<UserLikesSong> implements UserLikesSongDAO {

    public UserLikesSongDAOImpl(Class<UserLikesSong> entityClass) {
        super(entityClass);
    }

    @Override
    public UserLikesSong findByUserAndSong(User user, Song song) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<UserLikesSong> value = service.submit(()->{
            try (Session session = HibernateUtil.getSessionFactory().openSession();){
                Query<UserLikesSong> query = session.createQuery("from UserLikesSong where user.id = :userID and song.id = :songID",
                                UserLikesSong.class)
                        .setParameter("userID",user.getId())
                        .setParameter("songID",song.getId());
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

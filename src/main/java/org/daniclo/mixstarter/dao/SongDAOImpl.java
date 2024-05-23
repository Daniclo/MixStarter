package org.daniclo.mixstarter.dao;

import org.daniclo.mixstarter.model.Album;
import org.daniclo.mixstarter.model.Song;
import org.daniclo.mixstarter.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SongDAOImpl extends GenericDAOImpl<Song> implements SongDAO {
    public SongDAOImpl(Class<Song> entityClass) {
        super(entityClass);
    }

    @Override
    public List<Song> getSongs() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<List<Song>> value = service.submit(()->{
            try (Session session = HibernateUtil.getSessionFactory().openSession();){
                Query<Song> query = session.createQuery("from Song", Song.class);
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
}

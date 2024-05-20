package org.daniclo.mixstarter.dao;

import org.daniclo.mixstarter.model.Album;
import org.daniclo.mixstarter.model.Song;
import org.daniclo.mixstarter.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class SongDAOImpl extends GenericDAOImpl<Song> implements SongDAO {
    public SongDAOImpl(Class<Song> entityClass) {
        super(entityClass);
    }

    @Override
    public List<Song> getSongs() {
        try (Session session = HibernateUtil.getSessionFactory().openSession();){
            Query<Song> query = session.createQuery("from Song", Song.class);
            return query.getResultList();
        }
    }
}

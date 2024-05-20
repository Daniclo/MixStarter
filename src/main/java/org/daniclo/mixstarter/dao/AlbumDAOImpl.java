package org.daniclo.mixstarter.dao;

import org.daniclo.mixstarter.model.Album;
import org.daniclo.mixstarter.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class AlbumDAOImpl extends GenericDAOImpl<Album> implements AlbumDAO {
    public AlbumDAOImpl(Class<Album> entityClass) {
        super(entityClass);
    }

    @Override
    public List<Album> getAlbums() {
        try (Session session = HibernateUtil.getSessionFactory().openSession();){
            Query<Album> query = session.createQuery("from Album", Album.class);
            return query.getResultList();
        }
    }
}
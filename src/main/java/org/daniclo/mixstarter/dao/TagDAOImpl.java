package org.daniclo.mixstarter.dao;

import org.daniclo.mixstarter.model.Album;
import org.daniclo.mixstarter.model.Tag;
import org.daniclo.mixstarter.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class TagDAOImpl extends GenericDAOImpl<Tag> implements TagDAO {
    public TagDAOImpl(Class<Tag> entityClass) {
        super(entityClass);
    }

    @Override
    public List<Tag> getTags() {
        try (Session session = HibernateUtil.getSessionFactory().openSession();){
            Query<Tag> query = session.createQuery("from Tag", Tag.class);
            return query.getResultList();
        }
    }
}

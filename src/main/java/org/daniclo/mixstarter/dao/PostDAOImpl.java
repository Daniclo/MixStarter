package org.daniclo.mixstarter.dao;

import org.daniclo.mixstarter.model.Album;
import org.daniclo.mixstarter.model.Post;
import org.daniclo.mixstarter.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class PostDAOImpl extends GenericDAOImpl<Post> implements PostDAO{
    public PostDAOImpl(Class<Post> entityClass) {
        super(entityClass);
    }

    @Override
    public List<Post> getPosts() {
        try (Session session = HibernateUtil.getSessionFactory().openSession();){
            Query<Post> query = session.createQuery("from Post", Post.class);
            return query.getResultList();
        }
    }
}

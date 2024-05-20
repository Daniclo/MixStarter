package org.daniclo.mixstarter.dao;

import org.daniclo.mixstarter.model.Album;
import org.daniclo.mixstarter.model.Comment;
import org.daniclo.mixstarter.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class CommentDAOImpl extends GenericDAOImpl<Comment> implements CommentDAO{
    public CommentDAOImpl(Class<Comment> entityClass) {
        super(entityClass);
    }

    @Override
    public List<Comment> getComments() {
        try (Session session = HibernateUtil.getSessionFactory().openSession();){
            Query<Comment> query = session.createQuery("from Comment", Comment.class);
            return query.getResultList();
        }
    }
}

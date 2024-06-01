package org.daniclo.mixstarter.data;

import org.daniclo.mixstarter.model.Comment;
import org.daniclo.mixstarter.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CommentDAOImpl extends GenericDAOImpl<Comment> implements CommentDAO{
    public CommentDAOImpl(Class<Comment> entityClass) {
        super(entityClass);
    }

    @Override
    public List<Comment> getComments() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<List<Comment>> value = service.submit(()->{
            try (Session session = HibernateUtil.getSessionFactory().openSession()){
                Query<Comment> query = session.createQuery("from Comment", Comment.class);
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
}

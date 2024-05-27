package org.daniclo.mixstarter.dao;

import org.daniclo.mixstarter.model.Album;
import org.daniclo.mixstarter.model.Tag;
import org.daniclo.mixstarter.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TagDAOImpl extends GenericDAOImpl<Tag> implements TagDAO {
    public TagDAOImpl(Class<Tag> entityClass) {
        super(entityClass);
    }

    @Override
    public List<Tag> getTags() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<List<Tag>> value = service.submit(()->{
            try (Session session = HibernateUtil.getSessionFactory().openSession();){
                Query<Tag> query = session.createQuery("from Tag", Tag.class);
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

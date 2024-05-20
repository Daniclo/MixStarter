package org.daniclo.mixstarter.dao;

import org.daniclo.mixstarter.model.Album;
import org.daniclo.mixstarter.model.User;
import org.daniclo.mixstarter.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class UserDAOImpl extends GenericDAOImpl<User> implements UserDAO{
    public UserDAOImpl(Class<User> entityClass) {
        super(entityClass);
    }

    @Override
    public List<User> getUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession();){
            Query<User> query = session.createQuery("from User", User.class);
            return query.getResultList();
        }
    }
}

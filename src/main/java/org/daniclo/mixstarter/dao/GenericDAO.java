package org.daniclo.mixstarter.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDAO<T> {

    Optional<T> findById(Long id);

    void create(T entity);

    void save(T entity);

    void deleteById(Long id);

    void delete(T entity);
}
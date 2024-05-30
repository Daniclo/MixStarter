package org.daniclo.mixstarter.data;

import org.daniclo.mixstarter.model.Song;

import java.util.Optional;

public interface GenericDAO<T> {

    Optional<T> findById(Long id);

    void create(T entity);

    T save(T entity);

    void deleteById(Long id);

    void delete(T entity);
}
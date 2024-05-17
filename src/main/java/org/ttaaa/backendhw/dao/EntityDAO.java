package org.ttaaa.backendhw.dao;

import java.util.List;

public interface EntityDAO<T, ID> {
    T insert(T entity);

    T update(ID id, T entity);

    T findById(ID id);

    List<T> findAll();

    void deleteById(ID id);
}

package org.ttaaa.backendhw.repository;

import java.util.List;

public interface EntityRepository<T, ID> {
    T save(T entity);

    T update(T entity);

    T getById(ID id);

    List<T> getAll();

    void deleteById(ID id);
}

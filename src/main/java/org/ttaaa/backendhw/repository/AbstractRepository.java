package org.ttaaa.backendhw.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public abstract class AbstractRepository {
    @PersistenceContext
    protected EntityManager entityManager;
}

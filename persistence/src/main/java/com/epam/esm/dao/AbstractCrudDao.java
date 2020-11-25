package com.epam.esm.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public abstract class AbstractCrudDao<T, ID> implements CrudDao<T, ID> {
    @PersistenceContext
    protected EntityManager em;

    private static final String ID = "id";

    public abstract Class<T> getType();

    @Override
    public List<T> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(getType());
        Root<T> root = cq.from(getType());
        cq.select(root);
        TypedQuery<T> query = em.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public Optional<T> findById(ID id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(getType());
        Root<T> root = cq.from(getType());
        cq.select(root)
                .where(
                        cb.equal(root.get(ID), id)
                );
        TypedQuery<T> query = em.createQuery(cq);
        return query.getResultStream()
                .findFirst();
    }

    @Override
    public T save(T entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public void deleteById(ID id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<T> delete = cb.createCriteriaDelete(getType());
        Root<T> certificateRoot = delete.from(getType());
        delete.where(
                cb.equal(certificateRoot.get(ID), id)
        );
        em.createQuery(delete)
                .executeUpdate();
    }

    @Override
    public T update(T entity) {
        return this.em.merge(entity);
    }

    @Override
    public void delete(T entity) {
        em.remove(entity);
    }
}

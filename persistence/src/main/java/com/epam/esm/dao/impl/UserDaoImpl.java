package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserDaoImpl implements UserDao {
    @PersistenceContext
    private final EntityManager em;

    private static final String ID = "id";
    private static final String EMAIL = "email";

    @Override
    public List<User> findAll(int page, int size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.select(root);
        TypedQuery<User> query = em.createQuery(cq);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    public Optional<User> findById(long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.select(root)
                .where(
                        cb.equal(root.get(ID), id)
                );
        TypedQuery<User> query = em.createQuery(cq);
        return query.getResultStream()
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.select(root)
                .where(
                        cb.equal(root.get(EMAIL), email)
                );
        TypedQuery<User> query = em.createQuery(cq);
        return query.getResultStream()
                .findFirst();
    }
}

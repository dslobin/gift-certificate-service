package com.epam.esm.dao.impl;

import com.epam.esm.dao.AbstractCrudDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl extends AbstractCrudDao<User, Long> implements UserDao {
    private static final String EMAIL = "email";

    @Override
    protected Class<User> getType() {
        return User.class;
    }

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

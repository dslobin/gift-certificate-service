package com.epam.esm.dao.impl;

import com.epam.esm.dao.CartDao;
import com.epam.esm.entity.Cart;
import com.epam.esm.entity.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CartDaoImpl implements CartDao {
    @PersistenceContext
    private final EntityManager em;

    private final SessionFactory sessionFactory;

    private static final String ID = "id";

    private static final String USER = "user";
    private static final String EMAIL = "email";

    @Override
    public Optional<Cart> findById(long id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Cart> cr = cb.createQuery(Cart.class);
        Root<Cart> root = cr.from(Cart.class);
        cr.select(root)
                .where(
                        cb.equal(root.get(ID), id)
                );
        TypedQuery<Cart> query = em.createQuery(cr);
        return query.getResultStream()
                .findFirst();
    }

    @Override
    public Cart save(Cart cart) {
        Session session = sessionFactory.getCurrentSession();
        long savedCartId = (long) session.save(cart);
        cart.setId(savedCartId);
        return cart;
    }

    @Override
    public Cart findByUserEmail(String userEmail) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Cart> cr = cb.createQuery(Cart.class);
        Root<Cart> root = cr.from(Cart.class);
        Join<Cart, User> userJoin = root.join(USER, JoinType.INNER);
        cr.select(root)
                .where(
                        cb.equal(userJoin.get(EMAIL), userEmail)
                );
        TypedQuery<Cart> query = em.createQuery(cr);
        return query.getResultList()
                .get(0);
    }
}

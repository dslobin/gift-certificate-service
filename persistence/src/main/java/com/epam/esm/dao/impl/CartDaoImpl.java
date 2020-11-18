package com.epam.esm.dao.impl;

import com.epam.esm.dao.CartDao;
import com.epam.esm.entity.Cart;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CartDaoImpl implements CartDao {
    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private final EntityManager em;

    private final SessionFactory sessionFactory;

    private static final String ID = "id";

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
}

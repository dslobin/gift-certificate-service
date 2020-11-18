package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class OrderDaoImpl implements OrderDao {
    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private final EntityManager em;

    private final SessionFactory sessionFactory;

    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String USER = "user";

    @Override
    public List<Order> findByUserEmail(String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> root = cq.from(Order.class);
        Join<Order, User> userJoin = root.join(USER, JoinType.INNER);
        cq.select(root)
                .where(
                        cb.equal(userJoin.get(EMAIL), email)
                );
        TypedQuery<Order> query = em.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public Optional<Order> findByIdAndUserEmail(long orderId, String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cr = cb.createQuery(Order.class);
        Root<Order> root = cr.from(Order.class);
        Join<Order, User> userJoin = root.join(USER, JoinType.INNER);
        cr.select(root)
                .where(
                        cb.equal(userJoin.get(EMAIL), email),
                        cb.equal(root.get(ID), orderId)
                );
        TypedQuery<Order> query = em.createQuery(cr);
        return query.getResultStream()
                .findFirst();
    }

    @Override
    public void save(Order order) {
        Session session = sessionFactory.getCurrentSession();
        session.save(order);
    }
}

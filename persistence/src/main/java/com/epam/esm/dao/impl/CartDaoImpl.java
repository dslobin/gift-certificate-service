package com.epam.esm.dao.impl;

import com.epam.esm.dao.AbstractCrudDao;
import com.epam.esm.dao.CartDao;
import com.epam.esm.entity.Cart;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

@Repository
public class CartDaoImpl extends AbstractCrudDao<Cart, Long> implements CartDao {
    private static final String USER = "user";
    private static final String EMAIL = "email";

    @Override
    public Class<Cart> getType() {
        return Cart.class;
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

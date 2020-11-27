package com.epam.esm.dao.impl;

import com.epam.esm.dao.AbstractCrudDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class TagDaoImpl extends AbstractCrudDao<Tag, Long> implements TagDao {
    private static final String NAME = "name";

    @Override
    protected Class<Tag> getType() {
        return Tag.class;
    }

    @Override
    public Set<Tag> findAll(int page, int size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tag> cq = cb.createQuery(Tag.class);
        Root<Tag> root = cq.from(Tag.class);
        cq.select(root);
        TypedQuery<Tag> query = em.createQuery(cq);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        List<Tag> tags = query.getResultList();
        return new HashSet<>(tags);
    }

    @Override
    public Optional<Tag> findByName(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tag> cq = cb.createQuery(Tag.class);
        Root<Tag> root = cq.from(Tag.class);
        cq.select(root)
                .where(
                        cb.equal(root.get(NAME), name)
                );
        TypedQuery<Tag> query = em.createQuery(cq);
        return query.getResultStream()
                .findFirst();
    }
}

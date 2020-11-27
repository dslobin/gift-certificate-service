package com.epam.esm.dao.impl;

import com.epam.esm.dao.AbstractCrudDao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dto.CertificateSearchCriteria;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Repository
@Slf4j
public class GiftCertificateDaoImpl extends AbstractCrudDao<GiftCertificate, Long> implements GiftCertificateDao {
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String CREATE_DATE = "createDate";
    private static final String TAGS = "tags";

    private static final String SORT_DELIMITER = ",";
    private static final String PERCENT_SIGN = "%";

    @Override
    protected Class<GiftCertificate> getType() {
        return GiftCertificate.class;
    }

    @Override
    public List<GiftCertificate> findAll(int page, int size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> cq = cb.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = cq.from(GiftCertificate.class);
        cq.select(root);
        TypedQuery<GiftCertificate> query = em.createQuery(cq);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    public List<GiftCertificate> findAll(
            CertificateSearchCriteria searchCriteria,
            int page,
            int size
    ) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);

        List<Predicate> predicates = prepareWhereClause(
                criteriaBuilder,
                criteriaQuery,
                root,
                searchCriteria.getTags(),
                searchCriteria.getName(),
                searchCriteria.getDescription()
        );
        List<Order> orders = prepareOrderClause(
                criteriaBuilder,
                root,
                searchCriteria.getSortByName(),
                searchCriteria.getSortByCreateDate()
        );
        criteriaQuery.select(root)
                .where(predicates.toArray(new Predicate[0]))
                .orderBy(orders);

        TypedQuery<GiftCertificate> query = em.createQuery(criteriaQuery);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    private List<Predicate> prepareWhereClause(
            CriteriaBuilder cb,
            CriteriaQuery<GiftCertificate> cq,
            Root<GiftCertificate> root,
            Set<String> tags,
            String name,
            String description
    ) {
        List<Predicate> predicates = new ArrayList<>();
        if (tags != null && !tags.isEmpty()) {
            Join<GiftCertificate, Tag> tagJoin = root.join(TAGS, JoinType.INNER);
            predicates.add(tagJoin.get(NAME).in(tags));
            cq.distinct(true);
        }
        if (!StringUtils.isEmpty(name)) {
            predicates.add(cb.like(cb.upper(root.get(NAME)), PERCENT_SIGN + name.toUpperCase() + PERCENT_SIGN));
        }
        if (!StringUtils.isEmpty(description)) {
            predicates.add(cb.like(cb.upper(root.get(DESCRIPTION)), PERCENT_SIGN + description.toUpperCase() + PERCENT_SIGN));
        }
        return predicates;
    }

    private List<Order> prepareOrderClause(
            CriteriaBuilder cb,
            Root<GiftCertificate> root,
            String sortByName,
            String sortByDate
    ) {
        List<Order> orders = new ArrayList<>();
        if (!StringUtils.isEmpty(sortByName)) {
            Path<Object> fieldName = root.get(NAME);
            String orderDirection = getSortDirection(sortByName);
            Order nameOrder = getOrderBy(cb, fieldName, orderDirection);
            orders.add(nameOrder);
        }
        if (!StringUtils.isEmpty(sortByDate)) {
            Path<Object> fieldCreateDate = root.get(CREATE_DATE);
            String orderDirection = getSortDirection(sortByDate);
            Order createDateOrder = getOrderBy(cb, fieldCreateDate, orderDirection);
            orders.add(createDateOrder);
        }
        return orders;
    }

    private Order getOrderBy(
            CriteriaBuilder cb,
            Path<Object> path,
            String direction
    ) {
        Sort.Direction sortDirection = Sort.Direction
                .fromOptionalString(direction)
                .orElse(Sort.Direction.ASC);
        Order orderBy;
        if (Sort.Direction.ASC.equals(sortDirection)) {
            orderBy = cb.asc(path);
        } else {
            orderBy = cb.desc(path);
        }
        return orderBy;
    }

    private String getSortDirection(String sort) {
        String[] params = sort.split(SORT_DELIMITER);
        if (params.length > 1) {
            return params[1];
        }
        return Sort.Direction.ASC.name();
    }
}

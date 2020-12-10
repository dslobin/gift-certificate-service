package com.epam.esm.specification;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.util.Set;

public class GiftCertificateSpecification {
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String TAGS = "tags";

    private static final String PERCENT_SIGN = "%";

    private GiftCertificateSpecification() {
    }

    public static Specification<GiftCertificate> certificateNameLike(String name) {
        return (root, query, cb) -> cb.like(
                cb.upper(root.get(NAME)), PERCENT_SIGN + name.toUpperCase() + PERCENT_SIGN
        );
    }

    public static Specification<GiftCertificate> certificateDescriptionLike(String description) {
        return (root, query, cb) -> cb.like(
                cb.upper(root.get(DESCRIPTION)), PERCENT_SIGN + description.toUpperCase() + PERCENT_SIGN
        );
    }

    public static Specification<GiftCertificate> certificateTagsIn(Set<String> tags) {
        return (root, query, cb) -> {
            Join<GiftCertificate, Tag> tagJoin = root.join(TAGS, JoinType.INNER);
            return tagJoin.get(NAME).in(tags);
        };
    }
}

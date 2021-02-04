package com.epam.esm.service.impl;

import com.epam.esm.dto.CertificateSearchCriteria;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.specification.GiftCertificateSpecification;
import com.epam.esm.util.Translator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateRepository certificateRepository;
    private final TagService tagService;
    private final Translator translator;
    private final GiftCertificateSpecification certificateSpecification;

    private static final String NAME = "name";
    private static final String CREATE_DATE = "createDate";
    private static final String SORT_DELIMITER = ",";

    @Override
    @Transactional(readOnly = true)
    public List<GiftCertificate> findAll(
            CertificateSearchCriteria searchCriteria,
            Pageable pageable
    ) {
        Specification<GiftCertificate> specification = prepareWhereClause(
                searchCriteria.getName(),
                searchCriteria.getDescription(),
                searchCriteria.getTags()
        );
        List<Sort> sorts = prepareOrderClause(
                searchCriteria.getSortByName(),
                searchCriteria.getSortByCreateDate()
        );
        Pageable sortPageable = getPageable(pageable, sorts);
        Page<GiftCertificate> certificatePage = certificateRepository.findAll(specification, sortPageable);
        return certificatePage.getContent();
    }

    private Pageable getPageable(Pageable pageable, List<Sort> sorts) {
        if (sorts == null || sorts.isEmpty()) {
            return pageable;
        }
        Sort sort = createSort(sorts);
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }

    private Sort createSort(List<Sort> sorts) {
        Sort finalSort = sorts.get(0);
        for (Sort sort : sorts) {
            finalSort = finalSort.and(sort);
        }
        return finalSort;
    }

    private Specification<GiftCertificate> prepareWhereClause(
            String certificateName,
            String certificateDescription,
            Set<String> tags
    ) {
        List<Specification<GiftCertificate>> specifications = new ArrayList<>();
        if (tags == null || !tags.isEmpty()) {
            specifications.add(certificateSpecification.certificateTagsIn(tags));
        }
        if (!StringUtils.isEmpty(certificateName)) {
            specifications.add(certificateSpecification.certificateNameLike(certificateName));
        }
        if (!StringUtils.isEmpty(certificateDescription)) {
            specifications.add(certificateSpecification.certificateDescriptionLike(certificateDescription));
        }

        return createSpecification(specifications);
    }

    private Specification<GiftCertificate> createSpecification(List<Specification<GiftCertificate>> specifications) {
        if (specifications.isEmpty()) {
            return null;
        }
        Specification<GiftCertificate> finalSpecification = specifications.get(0);
        for (Specification<GiftCertificate> specification : specifications) {
            finalSpecification = finalSpecification.and(specification);
        }
        return finalSpecification;
    }

    private List<Sort> prepareOrderClause(
            String sortByName,
            String sortByDate
    ) {
        List<Sort> sorts = new ArrayList<>();
        if (!StringUtils.isEmpty(sortByDate)) {
            sorts.add(Sort.by(getSortDirection(sortByDate), CREATE_DATE));
        }
        if (!StringUtils.isEmpty(sortByName)) {
            sorts.add(Sort.by(getSortDirection(sortByName), NAME));
        }
        return sorts;
    }

    private Sort.Direction getSortDirection(String value) {
        String[] params = value.split(SORT_DELIMITER);
        if (params.length > 1) {
            Sort.Direction direction = Sort.Direction
                    .fromOptionalString(params[1])
                    .orElse(Sort.Direction.ASC);
            log.debug("Sort direction: {}", direction);
            return direction;
        }
        log.debug("Standard sorting (ASC) was used.");
        return Sort.Direction.ASC;
    }

    @Override
    @Transactional(readOnly = true)
    public GiftCertificate findById(long id)
            throws GiftCertificateNotFoundException {
        return certificateRepository.findById(id)
                .orElseThrow(() ->
                        new GiftCertificateNotFoundException(String.format(translator.toLocale("error.notFound.certificate"), id)));
    }

    @Override
    @Transactional
    public GiftCertificate create(GiftCertificate certificate) {
        fillCertificateTags(certificate);
        return certificateRepository.save(certificate);
    }

    @Override
    @Transactional
    public GiftCertificate update(GiftCertificate newCertificate)
            throws GiftCertificateNotFoundException {
        GiftCertificate existedCertificate = findById(newCertificate.getId());
        fillCertificateTags(newCertificate);
        BeanUtils.copyProperties(newCertificate, existedCertificate, getNullPropertyNames(newCertificate));
        certificateRepository.save(existedCertificate);
        return existedCertificate;
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }

    private void fillCertificateTags(GiftCertificate certificate) {
        Set<Tag> tags = certificate.getTags();
        if (tags == null) {
            return;
        }
        Set<Tag> certificateTags = new HashSet<>();
        tags.forEach(tag -> {
            Tag certificateTag = tagService.findByName(tag.getName()).orElse(tag);
            certificateTags.add(certificateTag);
        });
        certificate.setTags(certificateTags);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        certificateRepository.deleteById(id);
    }
}

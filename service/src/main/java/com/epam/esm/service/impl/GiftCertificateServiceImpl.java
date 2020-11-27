package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.CertificateSearchCriteria;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.service.GiftCertificateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.FeatureDescriptor;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;
    private final GiftCertificateMapper giftCertificateMapper;

    @Override
    @Transactional(readOnly = true)
    public List<GiftCertificateDto> findAll(
            int page,
            int size,
            CertificateSearchCriteria searchCriteria
    ) {
        List<GiftCertificate> certificates = giftCertificateDao.findAll(searchCriteria, page, size);
        return certificates.stream()
                .map(giftCertificateMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public GiftCertificateDto findById(long id)
            throws GiftCertificateNotFoundException {
        GiftCertificate byId = giftCertificateDao.findById(id)
                .orElseThrow(() -> new GiftCertificateNotFoundException(id));
        return giftCertificateMapper.toDto(byId);
    }

    @Override
    @Transactional
    public GiftCertificateDto create(GiftCertificateDto certificateDto) {
        GiftCertificate giftCertificate = giftCertificateMapper.toModel(certificateDto);
        Set<Tag> tags = fillCertificateTags(giftCertificate);
        giftCertificate.setTags(tags);
        giftCertificate.setCreateDate(ZonedDateTime.now());
        giftCertificate.setAvailable(true);
        GiftCertificate createdCertificate = giftCertificateDao.save(giftCertificate);
        return giftCertificateMapper.toDto(createdCertificate);
    }

    @Override
    @Transactional
    public GiftCertificateDto update(GiftCertificateDto certificateDto)
            throws GiftCertificateNotFoundException {
        Optional<GiftCertificate> certificateOptional = giftCertificateDao.findById(certificateDto.getId());
        if (!certificateOptional.isPresent()) {
            throw new GiftCertificateNotFoundException(certificateDto.getId());
        }
        GiftCertificate certificate = prepareCertificateForUpdate(certificateDto);
        GiftCertificate certificateToUpdate = certificateOptional.get();
        BeanUtils.copyProperties(certificate, certificateToUpdate, getNullPropertyNames(certificate));
        giftCertificateDao.update(certificateToUpdate);
        log.debug("Updated certificate {}", certificateToUpdate);
        return giftCertificateMapper.toDto(certificateToUpdate);
    }

    private GiftCertificate prepareCertificateForUpdate(GiftCertificateDto certificateDto) {
        GiftCertificate giftCertificate = giftCertificateMapper.toModel(certificateDto);
        Set<Tag> tags = fillCertificateTags(giftCertificate);
        giftCertificate.setTags(tags);
        giftCertificate.setLastUpdateDate(ZonedDateTime.now());
        return giftCertificate;
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }

    private Set<Tag> fillCertificateTags(GiftCertificate certificate) {
        Set<Tag> tags = certificate.getTags();
        if (tags == null) {
            return null;
        }
        Set<Tag> newCertificateTags = new HashSet<>();
        tags.forEach(tag -> {
            Optional<Tag> existedTag = tagDao.findByName(tag.getName());
            if (existedTag.isPresent()) {
                newCertificateTags.add(existedTag.get());
            } else {
                newCertificateTags.add(tag);
            }
        });
        log.debug("Certificate tags: {}", newCertificateTags);
        return newCertificateTags;
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        giftCertificateDao.deleteById(id);
    }
}

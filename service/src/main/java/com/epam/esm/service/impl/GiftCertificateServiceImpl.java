package com.epam.esm.service.impl;

import com.epam.esm.dao.certificate.GiftCertificateDao;
import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;

    @Override
    @Transactional(readOnly = true)
    public Optional<GiftCertificate> findById(long id) {
        return giftCertificateDao.findById(id);
    }

    @Override
    @Transactional
    public GiftCertificate create(GiftCertificate giftCertificate) {
        Set<Long> tagIds = new HashSet<>();
        giftCertificate.getTags().forEach(tag -> {
            Optional<Tag> tagFromDb = tagDao.findByName(tag.getName());
            if (tagFromDb.isPresent()) {
                long existedTagId = tagFromDb.get().getId();
                tagIds.add(existedTagId);
                tag.setId(existedTagId);
            } else {
                long insertedTagId = tagDao.save(tag.getName());
                tag.setId(insertedTagId);
                tagIds.add(insertedTagId);
            }
        });
        long certificateId = giftCertificateDao.save(giftCertificate);
        tagIds.forEach(tagId -> giftCertificateDao.saveCertificateTag(certificateId, tagId));

        giftCertificate.setId(certificateId);
        return giftCertificate;
    }

    @Override
    @Transactional
    public GiftCertificate update(GiftCertificate giftCertificate) {
        long certificateId = giftCertificate.getId();
        Set<Long> oldCertificateTagIds = tagDao.findAllByGiftCertificateId(certificateId).stream()
                .map(Tag::getId)
                .collect(Collectors.toSet());
        Set<Long> newTagIds = new HashSet<>();
        giftCertificate.getTags().forEach(tag -> {
            Optional<Tag> tagFromDb = tagDao.findByName(tag.getName());
            if (tagFromDb.isPresent()) {
                long existedTagId = tagFromDb.get().getId();
                oldCertificateTagIds.removeIf(tagId -> tagId == existedTagId);
                tag.setId(existedTagId);
            } else {
                long insertedTagId = tagDao.save(tag.getName());
                tag.setId(insertedTagId);
                newTagIds.add(insertedTagId);
            }
        });

        newTagIds.forEach(tagId -> giftCertificateDao.saveCertificateTag(certificateId, tagId));

        oldCertificateTagIds.forEach(tagId -> giftCertificateDao.deleteCertificateTag(certificateId, tagId));

        giftCertificateDao.update(giftCertificate);
        return giftCertificate;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GiftCertificate> findAll() {
        return giftCertificateDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GiftCertificate> findAll(String tag, String name, String description, String sort) {
        return giftCertificateDao.findAll(tag, name, description, sort);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        giftCertificateDao.deleteById(id);
    }
}

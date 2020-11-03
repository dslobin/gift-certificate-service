package com.epam.esm.service.impl;

import com.epam.esm.dao.certificate.GiftCertificateDao;
import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;

    @Override
    public Optional<GiftCertificate> findById(long id) {
        return giftCertificateDao.findById(id);
    }

    @Override
    public GiftCertificate create(GiftCertificate giftCertificate) {
        Set<Long> tagIds = new HashSet<>();
        giftCertificate.getTags().forEach(tag -> {
            Optional<Tag> existingTag = tagDao.findByName(tag.getName());
            if (existingTag.isPresent()) {
                tagIds.add(tag.getId());
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
    public List<GiftCertificate> findAll() {
        return giftCertificateDao.findAll();
    }

    @Override
    public List<GiftCertificate> findAll(String tag, String name, String description, String sort) {
        return giftCertificateDao.findAll(tag, name, description, sort);
    }

    @Override
    public void deleteById(long id) {
        giftCertificateDao.deleteById(id);
    }
}

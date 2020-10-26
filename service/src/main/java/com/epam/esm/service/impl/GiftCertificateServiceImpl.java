package com.epam.esm.service.impl;

import com.epam.esm.dao.certificate.GiftCertificateDao;
import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagDao tagDao) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
    }

    @Override
    public Optional<GiftCertificate> findById(long id) {
        return giftCertificateDao.findById(id);
    }

    @Override
    public void create(GiftCertificate giftCertificate, List<Tag> certificateTags) {
        List<Long> tagIds = new ArrayList<>();
        for (Tag tag : certificateTags) {
            Optional<Tag> existingTag = tagDao.findByName(tag.getName());
            if (existingTag.isPresent()) {
                tagIds.add(tag.getId());
            } else {
                long insertedTagId = tagDao.save(tag);
                tagIds.add(insertedTagId);
            }
        }
        long certificateId = giftCertificateDao.save(giftCertificate);
        for (Long tagId : tagIds) {
            giftCertificateDao.saveCertificateTag(certificateId, tagId);
        }
    }

    @Override
    public List<GiftCertificate> findAll() {
        return giftCertificateDao.findAll();
    }

    @Override
    public void deleteById(long id) {
        giftCertificateDao.deleteById(id);
    }
}

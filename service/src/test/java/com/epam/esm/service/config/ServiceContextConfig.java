package com.epam.esm.service.config;

import com.epam.esm.dao.certificate.GiftCertificateDao;
import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.service.impl.TagServiceImpl;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceContextConfig {
    @Bean
    public TagDao tagDao() {
        return Mockito.mock(TagDao.class);
    }

    @Bean
    public TagService tagService(TagDao tagDao) {
        return new TagServiceImpl(tagDao);
    }

    @Bean
    public GiftCertificateDao giftCertificateDao() {
        return Mockito.mock(GiftCertificateDao.class);
    }

    @Bean
    public GiftCertificateService giftCertificateService(
            GiftCertificateDao giftCertificateDao,
            TagDao tagDao
    ) {
        return new GiftCertificateServiceImpl(giftCertificateDao, tagDao);
    }
}

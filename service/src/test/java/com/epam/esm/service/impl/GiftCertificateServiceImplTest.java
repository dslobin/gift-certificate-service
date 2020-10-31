package com.epam.esm.service.impl;

import com.epam.esm.dao.certificate.GiftCertificateDao;
import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.security.cert.Certificate;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {
    private TagDao tagDao;
    private GiftCertificateDao certificateDao;
    private GiftCertificateService certificateService;

    @BeforeEach
    void setUp() {
        tagDao = mock(TagDao.class);
        certificateDao = mock(GiftCertificateDao.class);
        certificateService = new GiftCertificateServiceImpl(certificateDao, tagDao);
    }

    @Test
    void shouldReturnAllCertificates() {
        List<GiftCertificate> certificates = new ArrayList<>();
        certificates.add(new GiftCertificate(
                1L,
                "Culinary master class Italian cuisine",
                "A culinary master class is an opportunity to become a Chef of a real Italian restaurant for 3 hours.",
                BigDecimal.valueOf(95.00),
                ZonedDateTime.now(),
                ZonedDateTime.now().plusDays(3),
                Duration.ofDays(14),
                new HashSet<>()
        ));
        certificates.add(new GiftCertificate(
                2L,
                "EXPRESS ALLOY ON KAYAKES",
                "When you sit in the city, get bored of the routine and the lack of fresh impressions, you should throw something new into your life.",
                BigDecimal.valueOf(160.00),
                ZonedDateTime.now(),
                ZonedDateTime.now().plusDays(1),
                Duration.ofDays(30),
                new HashSet<>()
        ));

        given(certificateDao.findAll()).willReturn(certificates);

        List<GiftCertificate> expectedCertificates = certificateService.findAll();

        assertEquals(expectedCertificates, certificates);
    }

    @Test
    void shouldSaveCertificateSuccessfully() {
        GiftCertificate certificate = new GiftCertificate(
                1L,
                "Culinary master class Italian cuisine",
                "A culinary master class is an opportunity to become a Chef of a real Italian restaurant for 3 hours.",
                BigDecimal.valueOf(95.00),
                ZonedDateTime.now(),
                ZonedDateTime.now().plusDays(3),
                Duration.ofDays(14),
                new HashSet<>()
        );

        given(certificateDao.save(any(GiftCertificate.class))).willReturn(certificate.getId());

        GiftCertificate savedCertificate = certificateService.create(certificate, certificate.getTags());

        assertThat(savedCertificate).isNotNull();
        int wantedNumberOfInvocations = 1;
        verify(certificateDao, times(wantedNumberOfInvocations)).save(any(GiftCertificate.class));
    }

    @Test
    void shouldReturnCertificateById() {
        long certificateId = 1L;
        Tag tagBarAndRestaurant = new Tag(1L, "bars_and_restaurants");
        Set<Tag> tags = new HashSet<>();
        tags.add(tagBarAndRestaurant);
        GiftCertificate certificate = new GiftCertificate(
                1L,
                "Culinary master class Italian cuisine",
                "A culinary master class is an opportunity to become a Chef of a real Italian restaurant for 3 hours.",
                BigDecimal.valueOf(95.00),
                ZonedDateTime.now(),
                ZonedDateTime.now().plusDays(3),
                Duration.ofDays(14),
                tags
        );

        given(certificateDao.findById(certificateId)).willReturn(Optional.of(certificate));

        Optional<GiftCertificate> expectedCertificate = certificateService.findById(certificateId);

        assertThat(expectedCertificate).isNotNull();
    }

    @Test
    void shouldDeleteCertificate() {
        long certificateId = 1L;

        certificateService.deleteById(certificateId);

        int wantedNumberOfInvocations = 1;
        verify(certificateDao, times(wantedNumberOfInvocations)).deleteById(certificateId);
    }
}

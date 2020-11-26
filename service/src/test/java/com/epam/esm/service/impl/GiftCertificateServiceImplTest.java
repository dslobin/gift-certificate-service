package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dto.CertificateSearchCriteria;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.config.ServiceContextTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = ServiceContextTest.class)
class GiftCertificateServiceImplTest {
    @Autowired
    private GiftCertificateDao certificateDao;
    @Autowired
    private GiftCertificateMapper certificateMapper;
    @Autowired
    private GiftCertificateService certificateService;

    @Test
    void givenCertificates_whenFindAllWithCertificateCriteria_thenGetCorrectCertificateSize() {
        List<GiftCertificate> certificates = getCertificates();
        int page = 1;
        int size = 10;
        Set<String> searchedTags = Stream.of("tag1", "tag2").collect(Collectors.toSet());
        CertificateSearchCriteria criteria = new CertificateSearchCriteria(searchedTags, "param1", "param2", "name,asc", "createDate,desc");
        given(certificateDao.findAll(criteria, page, size)).willReturn(certificates);

        List<GiftCertificateDto> actualCertificates = certificateService.findAll(page, size, criteria);

        int expectedCertificatesSize = certificates.size();
        int actualCertificatesSize = actualCertificates.size();
        assertEquals(expectedCertificatesSize, actualCertificatesSize);
    }

    private List<GiftCertificate> getCertificates() {
        return Arrays.asList(
                new GiftCertificate(
                        1L,
                        "Culinary master class Italian cuisine",
                        "A culinary master class is an opportunity to become a Chef of a real Italian restaurant for 3 hours.",
                        BigDecimal.valueOf(95.00),
                        ZonedDateTime.now(),
                        ZonedDateTime.now().plusDays(3),
                        Duration.ofDays(14),
                        new HashSet<>(),
                        true
                ),
                new GiftCertificate(
                        2L,
                        "Express alloy on kayakes",
                        "When you sit in the city, get bored of the routine and the lack of fresh impressions, you should throw something new into your life.",
                        BigDecimal.valueOf(160.00),
                        ZonedDateTime.now(),
                        ZonedDateTime.now().plusDays(1),
                        Duration.ofDays(30),
                        new HashSet<>(),
                        true
                )
        );
    }

    @Test
    void givenCertificateDto_whenSave_thenGetOk() {
        GiftCertificate certificate = new GiftCertificate(
                1L,
                "Culinary master class Italian cuisine",
                "A culinary master class is an opportunity to become a Chef of a real Italian restaurant for 3 hours.",
                BigDecimal.valueOf(95.00),
                ZonedDateTime.now(),
                ZonedDateTime.now().plusDays(3),
                Duration.ofDays(14),
                new HashSet<>(),
                true
        );

        given(certificateDao.save(any(GiftCertificate.class))).willReturn(certificate);

        GiftCertificateDto certificateDto = certificateMapper.toDto(certificate);
        GiftCertificateDto savedCertificate = certificateService.create(certificateDto);
        assertThat(savedCertificate).isNotNull();
    }

    @Test
    void givenCertificateDto_whenUpdate_thenGetOk() {
        GiftCertificate certificate = new GiftCertificate(
                1L,
                "Culinary master class Italian cuisine",
                "A culinary master class is an opportunity to become a Chef of a real Italian restaurant for 3 hours.",
                BigDecimal.valueOf(95.00),
                ZonedDateTime.now(),
                ZonedDateTime.now().plusDays(3),
                Duration.ofDays(14),
                new HashSet<>(),
                true
        );

        given(certificateDao.findById(certificate.getId())).willReturn(Optional.of(certificate));
        given(certificateDao.update(any(GiftCertificate.class))).willReturn(certificate);

        GiftCertificateDto certificateDto = certificateMapper.toDto(certificate);
        GiftCertificateDto updatedCertificate = certificateService.update(certificateDto);
        assertThat(updatedCertificate).isNotNull();
    }

    @Test
    void givenCertificate_whenFindById_thenGetCorrectCertificate() {
        Tag tagBarAndRestaurant = new Tag(1L, "bars_and_restaurants", null);
        Set<Tag> tags = Stream.of(
                tagBarAndRestaurant
        ).collect(Collectors.toSet());
        GiftCertificate certificate = new GiftCertificate(
                1L,
                "Culinary master class Italian cuisine",
                "A culinary master class is an opportunity to become a Chef of a real Italian restaurant for 3 hours.",
                BigDecimal.valueOf(95.00),
                ZonedDateTime.now(),
                ZonedDateTime.now().plusDays(3),
                Duration.ofDays(14),
                tags,
                true
        );

        long certificateId = 1L;
        given(certificateDao.findById(certificateId)).willReturn(Optional.of(certificate));

        GiftCertificateDto actualCertificate = certificateService.findById(certificateId);
        assertThat(actualCertificate).isNotNull();
    }

    @Test
    void givenCertificateId_whenDeleteById_thenGetOk() {
        long certificateId = 1L;

        certificateService.deleteById(certificateId);

        int wantedNumberOfInvocations = 1;
        verify(certificateDao, times(wantedNumberOfInvocations)).deleteById(certificateId);
    }
}

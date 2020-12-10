package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = ServiceContextTest.class)
class GiftCertificateServiceImplTest {
    @Autowired
    private GiftCertificateRepository certificateDao;
    @Autowired
    private TagService tagService;
    @Autowired
    private GiftCertificateService certificateService;

    private List<GiftCertificate> getCertificates() {
        Set<Tag> tags = Stream.of(
                new Tag(1L, "new", null),
                new Tag(2L, "exclusive", null),
                new Tag(3L, "travel", null)
        ).collect(Collectors.toSet());
        return Arrays.asList(
                new GiftCertificate(
                        1L,
                        "Culinary master class Italian cuisine",
                        "A culinary master class is an opportunity to become a Chef of a real Italian restaurant for 3 hours.",
                        BigDecimal.valueOf(95.00),
                        ZonedDateTime.now(),
                        ZonedDateTime.now().plusDays(3),
                        Duration.ofDays(14),
                        tags,
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
                        tags,
                        true
                )
        );
    }

    @Test
    void givenCertificateDto_whenSave_thenGetOk() {
        GiftCertificate certificate = getOneCertificate();

        given(certificateDao.save(any(GiftCertificate.class))).willReturn(certificate);

        GiftCertificate savedCertificate = certificateService.create(certificate);
        assertThat(savedCertificate).isNotNull();
    }

    @Test
    void givenCertificateDto_whenUpdate_thenGetOk() {
        GiftCertificate certificate = getOneCertificate();

        given(certificateDao.findById(certificate.getId())).willReturn(Optional.of(certificate));
        given(certificateDao.save(any(GiftCertificate.class))).willReturn(certificate);

        GiftCertificate updatedCertificate = certificateService.update(certificate);
        assertThat(updatedCertificate).isNotNull();
    }

    @Test
    void givenCertificate_whenFindById_thenGetCorrectCertificate() {
        GiftCertificate certificate = getOneCertificate();

        long certificateId = 1L;
        given(certificateDao.findById(certificateId)).willReturn(Optional.of(certificate));

        GiftCertificate actualCertificate = certificateService.findById(certificateId);
        assertThat(actualCertificate).isNotNull();
    }

    private GiftCertificate getOneCertificate() {
        Set<Tag> tags = Stream.of(
                new Tag(1L, "culinary", null),
                new Tag(2L, "exclusive", null)
        ).collect(Collectors.toSet());
        long id = 1L;
        String name = "Culinary master class Italian cuisine";
        String description = "A culinary master class is an opportunity to become a Chef of a real Italian restaurant for 3 hours.";
        BigDecimal price = BigDecimal.valueOf(95.00);
        ZonedDateTime createDate = ZonedDateTime.now();
        ZonedDateTime lastUpdateDate = createDate.plusDays(10);
        Duration duration = Duration.ofDays(14);
        boolean available = true;
        return new GiftCertificate(id, name, description, price, createDate, lastUpdateDate, duration, tags, available);
    }

    @Test
    void givenGiftCertificateId_whenFindById_thenThrowGiftCertificateNotFoundException() {
        long certificateId = 1L;
        given(certificateDao.findById(certificateId)).willReturn(Optional.empty());

        assertThrows(GiftCertificateNotFoundException.class, () -> {
            certificateService.findById(certificateId);
        });
    }

    @Test
    void givenCertificateId_whenDeleteById_thenGetOk() {
        long certificateId = 1L;

        certificateService.deleteById(certificateId);

        int wantedNumberOfInvocations = 1;
        verify(certificateDao, times(wantedNumberOfInvocations)).deleteById(certificateId);
    }
}

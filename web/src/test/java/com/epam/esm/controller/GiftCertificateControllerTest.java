package com.epam.esm.controller;

import com.epam.esm.config.ControllerContextTest;
import com.epam.esm.config.SpringApplicationInitializer;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.service.GiftCertificateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {
        ControllerContextTest.class,
        SpringApplicationInitializer.class,
})
@WebAppConfiguration
class GiftCertificateControllerTest {
    @Qualifier("giftCertificateService")
    @Autowired
    private GiftCertificateService certificateService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private GiftCertificateMapper certificateMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        Mockito.reset(certificateService);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void givenCertificates_whenGetAll_thenGetCorrectCount() throws Exception {
        Tag tagFirst = new Tag(1L, "first");
        Tag tagSecond = new Tag(2L, "second");
        Set<Tag> tags = Stream.of(tagFirst, tagSecond).collect(Collectors.toSet());
        GiftCertificate certificate1 = new GiftCertificate(1L, "Certificate 1", "Description 1", BigDecimal.valueOf(95.00), ZonedDateTime.now(), ZonedDateTime.now().plusDays(3), Duration.ofDays(14), tags);
        GiftCertificate certificate2 = new GiftCertificate(2L, "Certificate 2", "Description 2", BigDecimal.valueOf(100.25), ZonedDateTime.now(), null, Duration.ofDays(21), tags);
        List<GiftCertificate> certificates = Arrays.asList(certificate1, certificate2);

        when(certificateService.findAll(null, null, null, null, null)).thenReturn(certificates);

        mockMvc.perform(get("/api/certificates"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].tags", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[1].tags", Matchers.hasSize(2)));
    }

    @Test
    void givenGiftCertificateId_whenGetOne_thenReturnCorrectGiftCertificate() throws Exception {
        long giftCertificateId = 1;
        ZonedDateTime createDate = ZonedDateTime.now();
        ZonedDateTime lastUpdateDate = createDate.plusDays(7);
        Duration durationInDays = Duration.ofDays(21);
        GiftCertificate certificate = new GiftCertificate(giftCertificateId, "Certificate 1", "Description 1", BigDecimal.valueOf(80.99), createDate, lastUpdateDate, durationInDays, new HashSet<>());
        when(certificateService.findById(giftCertificateId)).thenReturn(Optional.of(certificate));

        mockMvc.perform(get("/api/certificates/{id}", giftCertificateId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(giftCertificateId), Long.class))
                .andExpect(jsonPath("$.name", Matchers.is("Certificate 1")))
                .andExpect(jsonPath("$.description", Matchers.is("Description 1")))
                .andExpect(jsonPath("$.price", Matchers.is(80.99)))
                .andExpect(jsonPath("$.tags", Matchers.hasSize(0)));
    }

    @Test
    void givenGiftCertificateId_whenDelete_thenReturnHttpStatusCode204() throws Exception {
        long certificateId = 1;

        when(certificateService.findById(any(Long.class))).thenReturn(Optional.of(new GiftCertificate()));

        mockMvc.perform(delete("/api/certificates/{id}", String.valueOf(certificateId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void givenGiftCertificateId_whenGetCertificateById_thenReturnHttpStatusCode404() throws Exception {
        long certificateId = 1L;
        when(certificateService.findById(certificateId)).thenThrow(new GiftCertificateNotFoundException(certificateId));

        mockMvc.perform(get("/api/certificates/{id}", certificateId))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenGiftCertificateId_whenGetCertificateById_thenReturnCorrectGiftCertificate() throws Exception {
        Tag tagFirst = new Tag(1L, "first");
        Tag tagSecond = new Tag(2L, "second");
        Set<Tag> tags = Stream.of(tagFirst, tagSecond).collect(Collectors.toSet());
        long certificateId = 1;
        GiftCertificate certificate = new GiftCertificate(certificateId, "Certificate 1", "Description 1", BigDecimal.valueOf(95.00), ZonedDateTime.now(), ZonedDateTime.now().plusDays(3), Duration.ofDays(14), tags);

        when(certificateService.findById(certificateId)).thenReturn(Optional.of(certificate));

        mockMvc.perform(get("/api/certificates/{id}", certificateId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(certificateId), Long.class))
                .andExpect(jsonPath("$.name", Matchers.is(certificate.getName())))
                .andExpect(jsonPath("$.description", Matchers.is(certificate.getDescription())))
                .andExpect(jsonPath("$.tags", Matchers.hasSize(certificate.getTags().size())));
    }

    @Test
    void givenGiftCertificate_whenCreate_thenReturnCreatedGiftCertificateDto() throws Exception {
        Tag tagFirst = new Tag(1L, "first");
        Tag tagSecond = new Tag(2L, "second");
        Set<Tag> tags = Stream.of(tagFirst, tagSecond).collect(Collectors.toSet());
        long certificateId = 1;
        GiftCertificate certificate = new GiftCertificate(certificateId, "Certificate 1", "Description 1", BigDecimal.valueOf(100.99), ZonedDateTime.now(), ZonedDateTime.now().plusDays(7), Duration.ofDays(5), tags);

        GiftCertificateDto certificateDto = certificateMapper.toDto(certificate);

        when(certificateService.create(any(GiftCertificate.class))).thenReturn(certificate);

        mockMvc.perform(post("/api/certificates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(certificateDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(certificateId), Long.class));
    }

    @Test
    void givenGiftCertificate_whenUpdate_thenReturnUpdatedGiftCertificateDto() throws Exception {
        Tag tagFirst = new Tag(1L, "first");
        Tag tagSecond = new Tag(2L, "second");
        Set<Tag> tags = Stream.of(tagFirst, tagSecond).collect(Collectors.toSet());
        long certificateId = 1;
        GiftCertificate certificate = new GiftCertificate(certificateId, "Certificate 1", "Description 1", BigDecimal.valueOf(100.99), ZonedDateTime.now(), ZonedDateTime.now().plusDays(7), Duration.ofDays(5), tags);

        GiftCertificateDto certificateDto = certificateMapper.toDto(certificate);

        when(certificateService.findById(any(Long.class))).thenReturn(Optional.of(certificate));

        when(certificateService.update(any(GiftCertificate.class))).thenReturn(certificate);

        mockMvc.perform(put("/api/certificates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(certificateDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(certificateId), Long.class));
    }
}

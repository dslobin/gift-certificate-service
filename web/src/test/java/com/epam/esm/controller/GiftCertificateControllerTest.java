package com.epam.esm.controller;

import com.epam.esm.dto.CertificateSearchCriteria;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.service.GiftCertificateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
class GiftCertificateControllerTest {
    @MockBean
    private GiftCertificateService certificateService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private GiftCertificateMapper certificateMapper;

    private static final String PARAM_PAGE = "page";
    private static final String PARAM_SIZE = "size";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_DESCRIPTION = "description";
    private static final String PARAM_SORT_BY_NAME = "sortByName";
    private static final String PARAM_SORT_BY_CREATE_DATE = "sortByCreateDate";
    private static final String PARAM_TAGS = "tags";

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    void givenCertificates_whenGetAll_thenGetCorrectCount() throws Exception {
        List<GiftCertificate> certificates = getCertificates();

        Set<String> searchedTags = Stream.of("tag1", "tag2", "tag3").collect(Collectors.toSet());
        CertificateSearchCriteria criteria = new CertificateSearchCriteria(searchedTags, "param1", "param2", "name,asc", "createDate,desc");
        int page = 0;
        int size = 100;
        PageRequest pageable = PageRequest.of(page, size);
        when(certificateService.findAll(criteria, pageable)).thenReturn(certificates);

        mockMvc.perform(get("/api/certificates")
                .param(PARAM_PAGE, String.valueOf(page))
                .param(PARAM_SIZE, String.valueOf(size))
                .param(PARAM_NAME, criteria.getName())
                .param(PARAM_DESCRIPTION, criteria.getDescription())
                .param(PARAM_SORT_BY_NAME, criteria.getSortByName())
                .param(PARAM_SORT_BY_CREATE_DATE, criteria.getSortByCreateDate())
                .param(PARAM_TAGS, searchedTags.toArray(new String[0]))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].tags", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[1].tags", Matchers.hasSize(3)));

        int wantedNumberOfInvocations = 1;
        verify(certificateService, times(wantedNumberOfInvocations)).findAll(criteria, pageable);
    }

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
    @WithMockUser(username = "test", password = "test", roles = "USER")
    void givenGiftCertificateId_whenGetOne_thenReturnCorrectGiftCertificate() throws Exception {
        long giftCertificateId = 1;
        GiftCertificate certificate = getOneCertificate();
        when(certificateService.findById(giftCertificateId)).thenReturn(certificate);

        mockMvc.perform(get("/api/certificates/{id}", giftCertificateId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(giftCertificateId), Long.class))
                .andExpect(jsonPath("$.name", Matchers.is("Culinary master class Italian cuisine")))
                .andExpect(jsonPath("$.description", Matchers.is("A culinary master class is an opportunity to become a Chef of a real Italian restaurant for 3 hours.")))
                .andExpect(jsonPath("$.price", Matchers.is(95.00)))
                .andExpect(jsonPath("$.tags", Matchers.hasSize(2)));
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    void givenGiftCertificateId_whenDelete_thenReturnHttpStatusCode204() throws Exception {
        GiftCertificate certificate = getOneCertificate();
        when(certificateService.findById(any(Long.class))).thenReturn(certificate);
        long certificateId = 1;
        mockMvc.perform(delete("/api/certificates/{id}", String.valueOf(certificateId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    void givenGiftCertificateId_whenGetCertificateById_thenReturnCorrectGiftCertificate() throws Exception {
        long giftCertificateId = 1;
        GiftCertificate certificate = getOneCertificate();

        when(certificateService.findById(giftCertificateId)).thenReturn(certificate);

        mockMvc.perform(get("/api/certificates/{id}", giftCertificateId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(giftCertificateId), Long.class))
                .andExpect(jsonPath("$.name", Matchers.is(certificate.getName())))
                .andExpect(jsonPath("$.description", Matchers.is(certificate.getDescription())))
                .andExpect(jsonPath("$.tags", Matchers.hasSize(certificate.getTags().size())));
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    void givenGiftCertificate_whenCreate_thenReturnCreatedGiftCertificateDto() throws Exception {
        long certificateId = 1;
        GiftCertificate certificate = getOneCertificate();

        when(certificateService.create(any(GiftCertificate.class))).thenReturn(certificate);

        GiftCertificateDto certificateDto = certificateMapper.toDto(certificate);
        mockMvc.perform(post("/api/certificates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(certificateDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(certificateId), Long.class));
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    void givenGiftCertificate_whenUpdate_thenReturnUpdatedGiftCertificateDto() throws Exception {
        long certificateId = 1;
        GiftCertificate certificate = getOneCertificate();

        when(certificateService.update(any(GiftCertificate.class))).thenReturn(certificate);

        GiftCertificateDto certificateDto = certificateMapper.toDto(certificate);
        mockMvc.perform(put("/api/certificates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(certificateDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(certificateId), Long.class));
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
}

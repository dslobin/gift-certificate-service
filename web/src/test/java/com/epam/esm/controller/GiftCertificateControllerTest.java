package com.epam.esm.controller;

import com.epam.esm.dto.CertificateSearchCriteria;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.GiftCertificateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(GiftCertificateController.class)
class GiftCertificateControllerTest {
    @MockBean
    private GiftCertificateService certificateService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final int PAGE = 1;
    private static final int SIZE = 5;
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_SIZE = "size";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_DESCRIPTION = "description";
    private static final String PARAM_SORT_BY_NAME = "sortByName";
    private static final String PARAM_SORT_BY_CREATE_DATE = "sortByCreateDate";
    private static final String PARAM_TAGS = "tags";

    @Test
    void givenCertificates_whenGetAll_thenGetCorrectCount() throws Exception {
        TagDto tagFirst = new TagDto(1L, "first");
        TagDto tagSecond = new TagDto(2L, "second");
        Set<TagDto> tags = Stream.of(tagFirst, tagSecond).collect(Collectors.toSet());
        GiftCertificateDto certificate1 = new GiftCertificateDto(1L, "Certificate 1", "Description 1", BigDecimal.valueOf(95.00), ZonedDateTime.now(), ZonedDateTime.now().plusDays(3), 14, tags);
        GiftCertificateDto certificate2 = new GiftCertificateDto(2L, "Certificate 2", "Description 2", BigDecimal.valueOf(100.25), ZonedDateTime.now(), null, 21, tags);
        List<GiftCertificateDto> certificates = Arrays.asList(certificate1, certificate2);

        Set<String> searchedTags = Stream.of("tag1", "tag2", "tag3").collect(Collectors.toSet());
        CertificateSearchCriteria criteria = new CertificateSearchCriteria(searchedTags, "param1", "param2", "name,asc", "createDate,desc");
        when(certificateService.findAll(PAGE, SIZE, criteria)).thenReturn(certificates);

        mockMvc.perform(get("/api/certificates")
                .param(PARAM_PAGE, String.valueOf(PAGE))
                .param(PARAM_SIZE, String.valueOf(SIZE))
                .param(PARAM_NAME, criteria.getName())
                .param(PARAM_DESCRIPTION, criteria.getDescription())
                .param(PARAM_SORT_BY_NAME, criteria.getSortByName())
                .param(PARAM_SORT_BY_CREATE_DATE, criteria.getSortByCreateDate())
                .param(PARAM_TAGS, searchedTags.toArray(new String[0]))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].tags", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[1].tags", Matchers.hasSize(2)));

        int wantedNumberOfInvocations = 1;
        verify(certificateService, times(wantedNumberOfInvocations)).findAll(PAGE, SIZE, criteria);
    }

    @Test
    void givenGiftCertificateId_whenGetOne_thenReturnCorrectGiftCertificate() throws Exception {
        long giftCertificateId = 1;
        ZonedDateTime createDate = ZonedDateTime.now();
        ZonedDateTime lastUpdateDate = createDate.plusDays(7);
        GiftCertificateDto certificate = new GiftCertificateDto(giftCertificateId, "Certificate 1", "Description 1", BigDecimal.valueOf(80.99), createDate, lastUpdateDate, 21, new HashSet<>());
        when(certificateService.findById(giftCertificateId)).thenReturn(certificate);

        mockMvc.perform(get("/api/certificates/{id}", giftCertificateId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(giftCertificateId), Long.class))
                .andExpect(jsonPath("$.name", Matchers.is("Certificate 1")))
                .andExpect(jsonPath("$.description", Matchers.is("Description 1")))
                .andExpect(jsonPath("$.price", Matchers.is(80.99)))
                .andExpect(jsonPath("$.tags", Matchers.hasSize(0)));
    }

    @Test
    void givenGiftCertificateId_whenDelete_thenReturnHttpStatusCode204() throws Exception {
        ZonedDateTime createDate = ZonedDateTime.now();
        ZonedDateTime lastUpdateDate = createDate.plusDays(7);
        GiftCertificateDto certificate = new GiftCertificateDto(1L, "Certificate 1", "Description 1", BigDecimal.valueOf(80.99), createDate, lastUpdateDate, 21, new HashSet<>());
        when(certificateService.findById(any(Long.class))).thenReturn(certificate);
        long certificateId = 1;
        mockMvc.perform(delete("/api/certificates/{id}", String.valueOf(certificateId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void givenGiftCertificateId_whenGetCertificateById_thenReturnCorrectGiftCertificate() throws Exception {
        TagDto tagFirst = new TagDto(1L, "first");
        TagDto tagSecond = new TagDto(2L, "second");
        Set<TagDto> tags = Stream.of(tagFirst, tagSecond).collect(Collectors.toSet());
        long certificateId = 1;
        GiftCertificateDto certificate = new GiftCertificateDto(certificateId, "Certificate 1", "Description 1", BigDecimal.valueOf(95.00), ZonedDateTime.now(), ZonedDateTime.now().plusDays(3), 14, tags);

        when(certificateService.findById(certificateId)).thenReturn(certificate);

        mockMvc.perform(get("/api/certificates/{id}", certificateId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(certificateId), Long.class))
                .andExpect(jsonPath("$.name", Matchers.is(certificate.getName())))
                .andExpect(jsonPath("$.description", Matchers.is(certificate.getDescription())))
                .andExpect(jsonPath("$.tags", Matchers.hasSize(certificate.getTags().size())));
    }

    @Test
    void givenGiftCertificate_whenCreate_thenReturnCreatedGiftCertificateDto() throws Exception {
        TagDto tagFirst = new TagDto(1L, "first");
        TagDto tagSecond = new TagDto(2L, "second");
        Set<TagDto> tags = Stream.of(tagFirst, tagSecond).collect(Collectors.toSet());
        long certificateId = 1;
        GiftCertificateDto certificate = new GiftCertificateDto(certificateId, "Certificate 1", "Description 1", BigDecimal.valueOf(100.99), ZonedDateTime.now(), ZonedDateTime.now().plusDays(7), 5, tags);

        when(certificateService.create(any(GiftCertificateDto.class))).thenReturn(certificate);

        mockMvc.perform(post("/api/certificates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(certificate)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(certificateId), Long.class));
    }

    @Test
    void givenGiftCertificate_whenUpdate_thenReturnUpdatedGiftCertificateDto() throws Exception {
        TagDto tagFirst = new TagDto(1L, "first");
        TagDto tagSecond = new TagDto(2L, "second");
        Set<TagDto> tags = Stream.of(tagFirst, tagSecond).collect(Collectors.toSet());
        long certificateId = 1;
        GiftCertificateDto certificate = new GiftCertificateDto(certificateId, "Certificate 1", "Description 1", BigDecimal.valueOf(100.99), ZonedDateTime.now(), ZonedDateTime.now().plusDays(7), 5, tags);

        when(certificateService.update(any(GiftCertificateDto.class))).thenReturn(certificate);

        mockMvc.perform(put("/api/certificates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(certificate)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(certificateId), Long.class));
    }
}

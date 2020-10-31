package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GiftCertificateDto {
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private ZonedDateTime createDate;
    private ZonedDateTime lastUpdateDate;
    private Long durationInDays;
    private Set<TagDto> tags;
}

package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GiftCertificate {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private ZonedDateTime createDate;
    private ZonedDateTime lastUpdateDate;
    private Duration duration;
    private Set<Tag> tags;
}

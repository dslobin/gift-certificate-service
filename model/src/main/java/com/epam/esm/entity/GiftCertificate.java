package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GiftCertificate {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private LocalDate createDate;
    private LocalDate lastUpdateDate;
    private Duration duration;
    private List<Tag> tags = new ArrayList<>();
}

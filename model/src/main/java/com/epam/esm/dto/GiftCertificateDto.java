package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GiftCertificateDto {
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private LocalDate createDate;
    private LocalDate lastUpdateDate;
    private Long durationInDays;
    private List<TagDto> tags = new ArrayList<>();
}

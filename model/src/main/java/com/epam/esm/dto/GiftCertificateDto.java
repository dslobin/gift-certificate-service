package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GiftCertificateDto {
    @PositiveOrZero
    private long id;

    @Size(min = 1, max = 512)
    private String name;

    @Size(min = 1, max = 1024)
    private String description;

    @Min(value = 1)
    @Max(value = 10_000)
    private BigDecimal price;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime lastUpdateDate;

    @PositiveOrZero
    @Max(value = 1826)
    private long durationInDays;

    private Set<TagDto> tags;
}

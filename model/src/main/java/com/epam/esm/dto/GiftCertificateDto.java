package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class GiftCertificateDto extends RepresentationModel<GiftCertificateDto> {
    @PositiveOrZero
    private long id;

    @Size(min = 1, max = 512)
    private String name;

    @Size(min = 1, max = 2048)
    private String description;

    @Min(value = 1)
    @Max(value = 10_000)
    private BigDecimal price;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime lastUpdateDate;

    // The maximum value is equivalent to 5 years
    @PositiveOrZero
    @Max(value = 1826)
    private long durationInDays;

    private Set<TagDto> tags = new HashSet<>();
}

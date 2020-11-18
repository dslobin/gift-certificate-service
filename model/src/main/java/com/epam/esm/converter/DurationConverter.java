package com.epam.esm.converter;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Duration;

@Converter(autoApply = true)
@Slf4j
public class DurationConverter implements AttributeConverter<Duration, Long> {

    @Override
    public Long convertToDatabaseColumn(Duration attribute) {
        log.info("Convert Duration to Long");
        return attribute.toDays();
    }

    @Override
    public Duration convertToEntityAttribute(Long duration) {
        log.info("Convert Long to Duration");
        return Duration.ofDays(duration);
    }
}

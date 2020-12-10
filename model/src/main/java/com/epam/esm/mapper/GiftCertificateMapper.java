package com.epam.esm.mapper;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GiftCertificateMapper {
    @Mapping(target = "durationInDays", expression = "java( certificate.getDuration() == null ? 0 : certificate.getDuration().toDays() )")
    GiftCertificateDto toDto(GiftCertificate certificate);

    @Mapping(target = "certificates", ignore = true)
    Tag toTagModel(TagDto tagDto);

    @Mapping(target = "available", ignore = true)
    @Mapping(target = "duration", expression = "java( java.time.Duration.ofDays( certificateDto.getDurationInDays() ) )")
    GiftCertificate toModel(GiftCertificateDto certificateDto);
}

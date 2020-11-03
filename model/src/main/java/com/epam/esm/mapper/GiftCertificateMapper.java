package com.epam.esm.mapper;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;

public interface GiftCertificateMapper {
    GiftCertificateDto toDto(GiftCertificate certificate);

    GiftCertificate toModel(GiftCertificateDto certificateDto);
}

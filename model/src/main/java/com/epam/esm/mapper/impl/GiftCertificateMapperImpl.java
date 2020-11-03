package com.epam.esm.mapper.impl;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.mapper.GiftCertificateMapper;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GiftCertificateMapperImpl implements GiftCertificateMapper {

    @Override
    public GiftCertificateDto toDto(GiftCertificate certificate) {
        if (certificate == null) {
            return null;
        }
        GiftCertificateDto dto = new GiftCertificateDto();
        dto.setId(certificate.getId());
        dto.setName(certificate.getName());
        dto.setDescription(certificate.getDescription());
        dto.setPrice(certificate.getPrice());
        dto.setCreateDate(certificate.getCreateDate());
        dto.setLastUpdateDate(certificate.getLastUpdateDate());
        dto.setDurationInDays(certificate.getDuration().toDays());
        Set<TagDto> tags = certificate.getTags().stream()
                .map(this::tagToTagDto)
                .collect(Collectors.toSet());
        dto.setTags(tags);
        return dto;
    }

    private TagDto tagToTagDto(Tag tag) {
        TagDto dto = new TagDto();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        return dto;
    }

    @Override
    public GiftCertificate toModel(GiftCertificateDto dto) {
        if (dto == null) {
            return null;
        }
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(dto.getId());
        certificate.setName(dto.getName());
        certificate.setDescription(dto.getDescription());
        certificate.setPrice(dto.getPrice());
        certificate.setCreateDate(dto.getCreateDate());
        certificate.setLastUpdateDate(dto.getLastUpdateDate());
        certificate.setDuration(Duration.ofDays(dto.getDurationInDays()));
        Set<Tag> tags = dto.getTags().stream()
                .map(this::tagDtoToTag)
                .collect(Collectors.toSet());
        certificate.setTags(tags);
        return certificate;
    }

    private Tag tagDtoToTag(TagDto dto) {
        Tag tag = new Tag();
        tag.setId(dto.getId());
        tag.setName(dto.getName());
        return tag;
    }
}

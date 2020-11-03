package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;

    /**
     * View gift certificates.
     *
     * @return gift certificates list
     */
    @GetMapping
    public ResponseEntity<List<GiftCertificateDto>> getAllCertificates(
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Set<String> sort
    ) {

        List<GiftCertificateDto> certificates = giftCertificateService.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(certificates);
    }

    private GiftCertificateDto convertToDto(GiftCertificate certificate) {
        GiftCertificateDto dto = new GiftCertificateDto();
        dto.setId(certificate.getId());
        dto.setName(certificate.getName());
        dto.setDescription(certificate.getDescription());
        dto.setPrice(certificate.getPrice());
        dto.setCreateDate(certificate.getCreateDate());
        dto.setLastUpdateDate(certificate.getLastUpdateDate());
        dto.setDurationInDays(certificate.getDuration().toDays());
        dto.setTags(certificate.getTags().stream()
                .map(this::convertToDto)
                .collect(Collectors.toSet())
        );
        return dto;
    }

    private TagDto convertToDto(Tag tag) {
        TagDto dto = new TagDto();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        return dto;
    }

    /**
     * Viewing a single gift certificate.
     *
     * @return gift certificate with the specified id
     * @throws GiftCertificateNotFoundException if the certificate with the specified id doesn't exist
     */
    @GetMapping("/${id}")
    public ResponseEntity<GiftCertificate> getOneCertificate(@PathVariable Long id) {
        GiftCertificate certificate = giftCertificateService.findById(id)
                .orElseThrow(() -> new GiftCertificateNotFoundException(id));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(certificate);
    }

    /**
     * Deleting a gift certificate.
     *
     * @return empty response
     * @throws GiftCertificateNotFoundException if the specified gift certificate does not exist
     */
    @DeleteMapping("/${id}")
    public ResponseEntity<Void> deleteCertificate(@PathVariable Long id) {
        GiftCertificate certificate = giftCertificateService.findById(id)
                .orElseThrow(() -> new GiftCertificateNotFoundException(id));
        giftCertificateService.deleteById(certificate.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Adding a gift certificate.
     *
     * @return updated gift certificate
     */
    @PostMapping
    public ResponseEntity<GiftCertificate> createCertificate(@RequestBody GiftCertificate certificate) {
        giftCertificateService.create(certificate, new HashSet<>());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(certificate);
    }
}

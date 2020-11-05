package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.service.GiftCertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateMapper certificateMapper;

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
            @RequestParam(required = false) String sort
    ) {
        List<GiftCertificateDto> certificates;
        if (areAllParamsEqualToNull(tag, name, description, sort)) {
            certificates = giftCertificateService.findAll().stream()
                    .map(certificateMapper::toDto)
                    .collect(Collectors.toList());
        } else {
            certificates = giftCertificateService.findAll(tag, name, description, sort).stream()
                    .map(certificateMapper::toDto)
                    .collect(Collectors.toList());
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(certificates);
    }

    private boolean areAllParamsEqualToNull(String... params) {
        for (String parameter : params) {
            if (parameter != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Viewing a single gift certificate.
     *
     * @return gift certificate with the specified id
     * @throws GiftCertificateNotFoundException if the certificate with the specified id doesn't exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> getOneCertificate(@PathVariable Long id) {
        GiftCertificate certificate = giftCertificateService.findById(id)
                .orElseThrow(() -> new GiftCertificateNotFoundException(id));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(certificateMapper.toDto(certificate));
    }

    /**
     * Deleting a gift certificate.
     *
     * @return empty response
     * @throws GiftCertificateNotFoundException if the specified gift certificate does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCertificate(@PathVariable Long id) {
        GiftCertificate certificate = giftCertificateService.findById(id)
                .orElseThrow(() -> new GiftCertificateNotFoundException(id));
        giftCertificateService.deleteById(certificate.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Adding a gift certificate.
     *
     * @return updated gift certificate
     */
    @PostMapping
    public ResponseEntity<GiftCertificateDto> createCertificate(@RequestBody GiftCertificateDto certificateDto) {
        GiftCertificate certificate = certificateMapper.toModel(certificateDto);
        GiftCertificate newCertificate = giftCertificateService.create(certificate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(certificateMapper.toDto(newCertificate));
    }

    /**
     * Updating a gift certificate.
     *
     * @return updated gift certificate
     * @throws GiftCertificateNotFoundException if the specified gift certificate does not exist
     */
    @PutMapping
    public ResponseEntity<GiftCertificateDto> updateCertificate(@RequestBody GiftCertificateDto certificateDto) {
        boolean isCertificatePresent = giftCertificateService.findById(certificateDto.getId()).isPresent();
        if (!isCertificatePresent) {
            throw new GiftCertificateNotFoundException(certificateDto.getId());
        }
        GiftCertificate certificate = certificateMapper.toModel(certificateDto);
        GiftCertificate updatedCertificate = giftCertificateService.update(certificate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(certificateMapper.toDto(updatedCertificate));
    }
}

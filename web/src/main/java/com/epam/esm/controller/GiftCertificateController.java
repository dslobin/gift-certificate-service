package com.epam.esm.controller;

import com.epam.esm.dto.CertificateSearchCriteria;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
@Validated
@Slf4j
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;

    /**
     * View gift certificates.
     *
     * @return gift certificates list
     */
    @GetMapping
    public ResponseEntity<List<GiftCertificateDto>> getCertificates(
            @Min(1) @RequestParam(required = false, defaultValue = "1") Integer page,
            @Min(5) @Max(100) @RequestParam(required = false, defaultValue = "10") Integer size,
            CertificateSearchCriteria searchCriteria
    ) {
        log.debug("Certificate search criteria params: {}", searchCriteria);
        List<GiftCertificateDto> certificates = giftCertificateService.findAll(page, size, searchCriteria);
        certificates.forEach(certificate -> certificate.add(linkTo(methodOn(GiftCertificateController.class)
                .getCertificate(certificate.getId()))
                .withSelfRel()));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(certificates);
    }

    /**
     * Viewing a single gift certificate.
     *
     * @return gift certificate with the specified id
     * @throws GiftCertificateNotFoundException if the certificate with the specified id doesn't exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> getCertificate(@Min(1) @PathVariable Long id)
            throws GiftCertificateNotFoundException {
        GiftCertificateDto certificateDto = giftCertificateService.findById(id);
        certificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                .getCertificates(1, 10, null))
                .withRel("certificates"));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(certificateDto);
    }

    /**
     * Adding a gift certificate.
     *
     * @return created gift certificate
     */
    @PostMapping
    public ResponseEntity<GiftCertificateDto> createCertificate(@Valid @RequestBody GiftCertificateDto certificateDto) {
        GiftCertificateDto createdCertificate = giftCertificateService.create(certificateDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdCertificate);
    }

    /**
     * Updating a gift certificate.
     *
     * @return updated gift certificate
     * @throws GiftCertificateNotFoundException if the specified gift certificate does not exist
     */
    @PutMapping
    public ResponseEntity<GiftCertificateDto> updateCertificate(@Valid @RequestBody GiftCertificateDto certificateDto)
            throws GiftCertificateNotFoundException {
        GiftCertificateDto updatedCertificate = giftCertificateService.update(certificateDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(updatedCertificate);
    }

    /**
     * Deleting a gift certificate.
     *
     * @return empty response
     * @throws GiftCertificateNotFoundException if the specified gift certificate does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCertificate(@Min(1) @PathVariable Long id)
            throws GiftCertificateNotFoundException {
        GiftCertificateDto certificateDto = giftCertificateService.findById(id);
        giftCertificateService.deleteById(certificateDto.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

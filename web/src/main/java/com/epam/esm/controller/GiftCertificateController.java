package com.epam.esm.controller;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.service.GiftCertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@RestController("/api/certificates")
@RequiredArgsConstructor
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;

    @GetMapping
    public ResponseEntity<List<GiftCertificate>> getAllCertificates(
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) LocalDate createDate,
            @RequestParam(required = false) Long duration
    ) {
        List<GiftCertificate> certificates = giftCertificateService.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(certificates);
    }

    @GetMapping("/${id}")
    public ResponseEntity<GiftCertificate> getOneCertificate(@PathVariable Long id) {
        GiftCertificate certificate = giftCertificateService.findById(id)
                .orElseThrow(() -> new GiftCertificateNotFoundException(id));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(certificate);
    }

    @DeleteMapping("/${id}")
    public ResponseEntity<Void> deleteCertificate(@PathVariable Long id) {
        GiftCertificate certificate = giftCertificateService.findById(id)
                .orElseThrow(() -> new GiftCertificateNotFoundException(id));
        giftCertificateService.deleteById(certificate.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GiftCertificate> createCertificate(@RequestBody GiftCertificate certificate) {
        giftCertificateService.create(certificate, new HashSet<>());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(certificate);
    }
}

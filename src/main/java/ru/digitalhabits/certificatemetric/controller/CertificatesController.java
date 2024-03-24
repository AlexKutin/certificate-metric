package ru.digitalhabits.certificatemetric.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.digitalhabits.certificatemetric.certificates.CertificateReader;
import ru.digitalhabits.certificatemetric.model.CertificateResponse;

import java.security.cert.X509Certificate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/certificates")
public class CertificatesController {
    private final CertificateReader certificateReader;


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CertificateResponse> getAllCertificates() {
        List<X509Certificate> x509Certificates = certificateReader.loadCertificates();
        return x509Certificates.stream()
                .map(CertificateResponse::fromX509Certificate)
                .toList();
    }
}

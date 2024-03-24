package ru.digitalhabits.certificatemetric.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.MultiGauge;
import io.micrometer.core.instrument.Tags;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.digitalhabits.certificatemetric.certificates.CertificateReader;
import ru.digitalhabits.certificatemetric.model.CertificateResponse;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CertificateMetric {
    private final MultiGauge certificateValidity;

    private final CertificateReader certificateReader;

    public CertificateMetric(MeterRegistry meterRegistry, CertificateReader certificateReader) {
        certificateValidity = MultiGauge.builder("cert.validity.days.count")
                .tag("cert_name", "validity")
                .register(meterRegistry);
        this.certificateReader = certificateReader;
    }

    @Scheduled(cron = "@hourly")
//    @Scheduled(fixedRateString = "1000", initialDelayString = "0")
    public void updateCertificateValidityGauge() {
        List<CertificateResponse> responseList = certificateReader.loadCertificates().stream()
                .map(CertificateResponse::fromX509Certificate)
                .toList();
        certificateValidity.register(
                responseList
                        .stream()
                        .map(certificateResponse ->
                                MultiGauge.Row.of(
                                        Tags.of("cert_name", certificateResponse.getCertName()),
                                        certificateResponse.getValidityPeriod()))
                        .collect(Collectors.toList())
                , true);
    }
}

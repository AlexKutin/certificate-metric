package ru.digitalhabits.certificatemetric.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class CertificateResponse {
    private String certName;
    private long validityPeriod;

    public static CertificateResponse fromX509Certificate(X509Certificate x509Certificate) {
        Date notAfter = x509Certificate.getNotAfter();
        LocalDate notAfterLocalDate = LocalDate.ofInstant(notAfter.toInstant(), ZoneId.systemDefault());
        long validityPeriod = ChronoUnit.DAYS.between(LocalDate.now(), notAfterLocalDate);

        return new CertificateResponse()
                .setCertName(x509Certificate.getSubjectX500Principal().getName())
                .setValidityPeriod(validityPeriod);
    }
}

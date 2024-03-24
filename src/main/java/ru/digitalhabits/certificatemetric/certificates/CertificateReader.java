package ru.digitalhabits.certificatemetric.certificates;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.*;
import java.util.List;
import java.util.Set;

@Component
public class CertificateReader {

    @Value("${key.store.password}")
    private String keyStorePassword;

    private KeyStore loadKeyCacertsStore() {
        String relativeCacertsPath = "/lib/security/cacerts".replace("/", File.separator);
        String filename = System.getProperty("java.home") + relativeCacertsPath;
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(filename);
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(fileInputStream, keyStorePassword.toCharArray());
            return keystore;
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new RuntimeException(e);
        }
    }

    public List<X509Certificate> loadCertificates() {
        KeyStore keyStore = loadKeyCacertsStore();
        PKIXParameters params;
        try {
            params = new PKIXParameters(keyStore);
            Set<TrustAnchor> trustAnchors = params.getTrustAnchors();
            return trustAnchors.stream()
                    .map(TrustAnchor::getTrustedCert).toList();
        } catch (KeyStoreException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.example.securedapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.saml2.core.Saml2X509Credential;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrations;
import org.springframework.security.web.SecurityFilterChain;

import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;

import static org.springframework.security.config.Customizer.withDefaults;

public class Saml2Logout {

    @Value("${private.key}")
    RSAPrivateKey key;
    @Value("${public.certificate}")
    X509Certificate certificate;

    @Bean
    RelyingPartyRegistrationRepository registrations() {
        Saml2X509Credential credential = Saml2X509Credential.signing(key, certificate);
        RelyingPartyRegistration registration = RelyingPartyRegistrations
                .fromMetadataLocation("https://ap.example.org/metadata")
                .registrationId("id")
                .singleLogoutServiceLocation("{baseUrl}/logout/saml2/slo")
                .signingX509Credentials((signing) -> signing.add(credential))
                .build();
        return new InMemoryRelyingPartyRegistrationRepository(registration);
    }

    @Bean
    SecurityFilterChain web(HttpSecurity http, RelyingPartyRegistrationRepository registrations) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().authenticated()
                )
                .saml2Login(withDefaults())
                .saml2Logout(withDefaults());

        return http.build();
    }
}

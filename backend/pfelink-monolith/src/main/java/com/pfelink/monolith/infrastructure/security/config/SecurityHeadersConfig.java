package com.pfelink.monolith.infrastructure.security.config;

import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.stereotype.Component;

@Component
public class SecurityHeadersConfig {

    public static void configureSecurityHeaders(HeadersConfigurer<?> headers) throws Exception {
        headers
            .contentTypeOptions(contentTypeOptions -> {})
            .xssProtection(xss -> xss
                .headerValue(org.springframework.security.web.header.writers.XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
            )
            .frameOptions(frameOptions -> frameOptions.deny())
            .httpStrictTransportSecurity(hsts -> hsts
                .maxAgeInSeconds(31536000)
                .includeSubDomains(true)
                .preload(true)
            )
            .contentSecurityPolicy(csp -> csp
                .policyDirectives("default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self'")
            )
            .referrerPolicy(referrer -> referrer.policy(org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
            .permissionsPolicy(permissions -> permissions
                .policy("camera=(), microphone=(), geolocation=()")
            );
    }
}

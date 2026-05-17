package com.pfelink.monolith.infrastructure.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import com.pfelink.monolith.domain.auth.repository.IUserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomAuthenticationEntryPoint authEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers("/api/auth/**")  // Public endpoints
            )
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(e -> e.authenticationEntryPoint(authEntryPoint))
            .headers(SecurityHeadersConfig::configureSecurityHeaders)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/faculty-action/**").permitAll()
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/api-docs/**").permitAll()
                // Health endpoint allowed for load balancers (unauthenticated)
                .requestMatchers("/actuator/health").permitAll()
                // Sensitive actuator endpoints require ADMIN role
                .requestMatchers("/actuator/metrics", "/actuator/metrics/**").hasRole("ADMIN")
                .requestMatchers("/actuator/prometheus").hasRole("ADMIN")
                // Other actuator endpoints require authentication
                .requestMatchers("/actuator/**").authenticated()
                .requestMatchers("/error", "/favicon.ico").permitAll()
                .requestMatchers("/api/v1/faculties/**", "/api/v1/advisors/**", "/api/v1/selection-requests/**", "/api/v1/uploads/**").permitAll()
                .requestMatchers("/api/v1/students/me").authenticated()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(IUserRepository userRepository) {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Specific origins only - update with production domains
        List<String> allowedOrigins = List.of(
            "http://localhost:5173",    // Frontend dev
            "http://localhost:3000",    // Alternative frontend dev
            "http://127.0.0.1:5173",
            "http://127.0.0.1:3000"
            // Add production domains here
            // "https://yourdomain.com"
        );
        config.setAllowedOrigins(allowedOrigins);

        // Specific methods only
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));

        // Specific headers only
        config.setAllowedHeaders(List.of(
            "Content-Type",
            "Authorization",
            "X-CSRF-Token",
            "X-Recaptcha-Token",
            "x-recaptcha-token"
        ));

        config.setExposedHeaders(List.of(
            "Authorization",
            "X-CSRF-Token"
        ));

        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}

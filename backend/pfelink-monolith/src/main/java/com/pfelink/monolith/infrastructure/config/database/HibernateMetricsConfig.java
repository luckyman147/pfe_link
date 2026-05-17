package com.pfelink.monolith.infrastructure.config.database;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures Micrometer metrics for Hibernate query performance monitoring.
 * Metrics are exposed at /actuator/metrics endpoint.
 */
@Configuration
@RequiredArgsConstructor
public class HibernateMetricsConfig {

    /**
     * Customize the meter registry to add custom Hibernate metrics
     */
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> hibernateMetricsCustomizer() {
        return registry -> {
            // Custom tags can be added here if needed
        };
    }

    /**
     * Timer for login request latency
     */
    @Bean(name = "loginTimer")
    public Timer loginTimer(MeterRegistry meterRegistry) {
        return Timer.builder("auth.login.duration")
                .description("Time taken for login operations")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    /**
     * Timer for database query latency
     */
    @Bean(name = "queryTimer")
    public Timer queryTimer(MeterRegistry meterRegistry) {
        return Timer.builder("db.query.duration")
                .description("Time taken for database queries")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }
}

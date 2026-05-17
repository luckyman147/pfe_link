package com.pfelink.monolith.infrastructure.config;

import com.pfelink.monolith.domain.academic.entity.faculty.Faculty;
import com.pfelink.monolith.domain.academic.repository.IFacultyRepository;
import com.pfelink.monolith.domain.auth.entity.User;
import com.pfelink.monolith.domain.auth.enums.AccountStatus;
import com.pfelink.monolith.domain.auth.enums.UserRole;
import com.pfelink.monolith.domain.auth.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final IUserRepository userRepository;
    private final com.pfelink.monolith.domain.academic.repository.ISeasonRepository seasonRepository;
    private final IFacultyRepository facultyRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.default-email:iyedtouati@gmail.com}")
    private String adminEmail;

    @Value("${app.admin.default-password:iyedtouati}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        // Seed Admin
        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setFullName("System Admin");
            admin.setRole(UserRole.ADMIN);
            admin.setStatus(AccountStatus.ACTIVE);
            admin.setEmailVerified(true);
           
            userRepository.save(admin);
            log.info("Default admin user created: {}", adminEmail);
        }

        // Seed Active Season
        if (seasonRepository.findActiveSeason().isEmpty()) {
            com.pfelink.monolith.domain.academic.entity.season.Season defaultSeason = new com.pfelink.monolith.domain.academic.entity.season.Season();
            defaultSeason.setName("Academic Year 2025-2026");
            defaultSeason.setStartDate(java.time.LocalDate.of(2025, 9, 1));
            defaultSeason.setEndDate(java.time.LocalDate.of(2026, 7, 31));
            defaultSeason.setActive(true);
            seasonRepository.save(defaultSeason);
            log.info("Default active academic season created: {}", defaultSeason.getName());
        }

        seedFacultyIfAbsent("Faculty of Sciences of Tunis", "FST",
            "contact@fst.utm.tn", "https://www.fst.rnu.tn");
        seedFacultyIfAbsent("National Institute of Applied Sciences and Technology", "INSAT",
            "contact@insat.rnu.tn", "https://www.insat.rnu.tn");
        seedFacultyIfAbsent("Faculty of Economics and Management of Tunis", "FSEGT",
            "contact@fsegt.rnu.tn", "https://www.fsegt.rnu.tn");
        seedFacultyIfAbsent("Higher Institute of Computer Science", "ISI",
            "contact@isi.rnu.tn", "https://www.isi.rnu.tn");
    }

    private void seedFacultyIfAbsent(String name, String abbreviation, String email, String websiteUrl) {
        if (facultyRepository.existsByName(name)) {
            return;
        }
        Faculty f = new Faculty();
        f.setName(name);
        f.setAbbreviation(abbreviation);
        f.setEmail(email);
        f.setWebsiteUrl(websiteUrl);
        f.setValidated(true);
        facultyRepository.save(f);
        log.info("Seeded faculty: {} ({})", name, abbreviation);
    }
}

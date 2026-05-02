package com.pfelink.monolith.infrastructure.persistence.auth;

import com.pfelink.monolith.domain.auth.entity.User;
import com.pfelink.monolith.domain.auth.enums.AccountStatus;
import com.pfelink.monolith.domain.auth.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SpringDataUserRepository userRepository;

    @Test
    void findByEmail_ShouldReturnUser_WhenExists() {
        // Arrange
        User user = new User();
        user.setEmail("test@me.com");
        user.setFullName("Test User");
        user.setPassword("secret");
        user.setRole(UserRole.STUDENT);
        user.setStatus(AccountStatus.ACTIVE);
        user.setTelephone("12345678");
        entityManager.persist(user);
        entityManager.flush();

        // Act
        Optional<User> found = userRepository.findByEmail("test@me.com");

        // Assert
        assertTrue(found.isPresent());
        assertEquals(user.getFullName(), found.get().getFullName());
    }

    @Test
    void existsByEmail_ShouldReturnTrue_WhenExists() {
        // Arrange
        User user = new User();
        user.setEmail("exist@me.com");
        user.setFullName("Exist User");
        user.setPassword("secret");
        user.setRole(UserRole.STUDENT);
        user.setStatus(AccountStatus.ACTIVE);
        user.setTelephone("87654321");
        entityManager.persist(user);
        entityManager.flush();

        // Act & Assert
        assertTrue(userRepository.existsByEmail("exist@me.com"));
        assertFalse(userRepository.existsByEmail("other@me.com"));
    }
}

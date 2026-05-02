package com.pfelink.monolith.infrastructure.persistence.academic;

import com.pfelink.monolith.domain.academic.entity.faculty.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.UUID;

public interface JpaAddressRepository extends JpaRepository<Address, UUID> {
    @Query(value = "SELECT * FROM addresses WHERE path = CAST(:path AS ltree)", nativeQuery = true)
    Optional<Address> findByPath(@Param("path") String path);
}

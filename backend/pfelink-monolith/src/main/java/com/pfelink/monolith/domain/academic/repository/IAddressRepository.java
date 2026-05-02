package com.pfelink.monolith.domain.academic.repository;

import com.pfelink.monolith.domain.academic.entity.faculty.Address;
import java.util.Optional;
import java.util.UUID;

public interface IAddressRepository {
    Optional<Address> findByPath(String path);
    Address save(Address address);
    Optional<Address> findById(UUID id);
}

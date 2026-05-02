package com.pfelink.monolith.infrastructure.persistence.academic;

import com.pfelink.monolith.domain.academic.entity.faculty.Address;
import com.pfelink.monolith.domain.academic.repository.IAddressRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
class PersistenceAddressRepository implements IAddressRepository {
    private final JpaAddressRepository jpaRepository;

    public PersistenceAddressRepository(JpaAddressRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Address> findByPath(String path) {
        return jpaRepository.findByPath(path);
    }

    @Override
    public Address save(Address address) {
        return jpaRepository.save(address);
    }

    @Override
    public Optional<Address> findById(UUID id) {
        return jpaRepository.findById(id);
    }
}

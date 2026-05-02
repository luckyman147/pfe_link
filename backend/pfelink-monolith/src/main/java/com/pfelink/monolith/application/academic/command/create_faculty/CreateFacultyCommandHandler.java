package com.pfelink.monolith.application.academic.command.create_faculty;

import com.pfelink.monolith.domain.academic.entity.faculty.Address;
import com.pfelink.monolith.domain.academic.entity.faculty.Faculty;
import com.pfelink.monolith.domain.academic.repository.IFacultyRepository;
import com.pfelink.monolith.shared.cqrs.ICommandHandler;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateFacultyCommandHandler 
        implements ICommandHandler<CreateFacultyCommand, Result<UUID>> {

    private final IFacultyRepository facultyRepository;
    private final com.pfelink.monolith.domain.academic.repository.IAddressRepository addressRepository;

    @Override
    @Transactional
    public Result<UUID> handle(CreateFacultyCommand cmd) {
        if (facultyRepository.existsByName(cmd.name())) {
            return Result.failure(com.pfelink.monolith.shared.result.Error.failure("Faculty.Exists", "Faculty already exists"));
        }

        // Search for address by path, or create if not exists
        Address address = addressRepository.findByPath(cmd.path())
            .orElseGet(() -> addressRepository.save(com.pfelink.monolith.shared.util.AddressParser.parse(cmd.path())));

        Faculty faculty = new Faculty();
        faculty.setName(cmd.name());
        faculty.setAbbreviation(cmd.abbreviation());
        faculty.setEmail(cmd.email());
        faculty.setWebsiteUrl(cmd.websiteUrl());
        faculty.setImageUrl(cmd.imageUrl());
        faculty.setAdminPassword(cmd.adminPassword());
        faculty.setAddress(address);

        Faculty saved = facultyRepository.save(faculty);
        return Result.success(saved.getId());
    }
}

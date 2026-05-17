package com.pfelink.monolith.application.academic.command.approve_faculty;

import com.pfelink.monolith.domain.academic.entity.faculty.Address;
import com.pfelink.monolith.domain.academic.entity.faculty.Faculty;
import com.pfelink.monolith.domain.academic.entity.faculty.PendingFaculty;
import com.pfelink.monolith.domain.academic.repository.IFacultyRepository;
import com.pfelink.monolith.domain.academic.repository.IPendingFacultyRepository;
import com.pfelink.monolith.infrastructure.event.events.faculty.FacultyApprovedEvent;
import com.pfelink.monolith.shared.cqrs.ICommandHandler;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApproveFacultyCommandHandler
        implements ICommandHandler<ApproveFacultyCommand, Result<UUID>> {

    private final IPendingFacultyRepository pendingRepo;
    private final IFacultyRepository facultyRepo;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    @CacheEvict(value = "faculties:page", allEntries = true)
    public Result<UUID> handle(ApproveFacultyCommand cmd) {
        PendingFaculty pending = pendingRepo.findById(cmd.pendingFacultyId())
            .orElse(null);

        if (pending == null) {
            return Result.failure(Error.failure("Faculty.NotFound", "Pending faculty not found"));
        }

        Address address = new Address();
        address.setPath(pending.getPath());
        address.setLatitude(pending.getLatitude());
        address.setLongitude(pending.getLongitude());
        address.setCountry(pending.getCountry());
        address.setCountryCode(pending.getCountryCode());
        address.setRegion(pending.getRegion());
        address.setGovernorate(pending.getGovernorate());
        address.setCity(pending.getCity());
        address.setSuburb(pending.getSuburb());
        address.setQuarter(pending.getQuarter());
        address.setNeighborhood(pending.getNeighborhood());
        address.setStreet(pending.getStreet());
        address.setHouseNumber(pending.getHouseNumber());
        address.setPostalCode(pending.getPostalCode());
        address.setDisplayName(pending.getDisplayName());

        Faculty faculty = new Faculty();
        faculty.setName(pending.getName());
        faculty.setAbbreviation(pending.getAbbreviation());
        faculty.setEmail(pending.getEmail());
        faculty.setWebsiteUrl(pending.getWebsiteUrl());
        faculty.setImageUrl(pending.getImageUrl());
        faculty.setAdminPassword(pending.getAdminPassword());
        faculty.setAddress(address);
        faculty.setValidated(true);

        Faculty saved = facultyRepo.save(faculty);
        pendingRepo.deleteById(pending.getId());

        eventPublisher.publishEvent(new FacultyApprovedEvent(
            saved.getName(), saved.getEmail()
        ));

        return Result.success(saved.getId());
    }
}

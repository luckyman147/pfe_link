package com.pfelink.monolith.application.academic.command.reject_faculty;

import com.pfelink.monolith.domain.academic.entity.faculty.PendingFaculty;
import com.pfelink.monolith.domain.academic.repository.IPendingFacultyRepository;
import com.pfelink.monolith.infrastructure.event.events.faculty.FacultyRejectedEvent;
import com.pfelink.monolith.shared.cqrs.ICommandHandler;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RejectFacultyCommandHandler
        implements ICommandHandler<RejectFacultyCommand, Result<Void>> {

    private final IPendingFacultyRepository pendingRepo;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public Result<Void> handle(RejectFacultyCommand cmd) {
        PendingFaculty pending = pendingRepo.findById(cmd.pendingFacultyId())
            .orElse(null);

        if (pending == null) {
            return Result.failure(Error.failure("Faculty.NotFound", "Pending faculty not found"));
        }

        String name = pending.getName();
        String email = pending.getEmail();

        pendingRepo.deleteById(cmd.pendingFacultyId());

        eventPublisher.publishEvent(new FacultyRejectedEvent(name, email));

        return Result.success(null);
    }
}

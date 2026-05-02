package com.pfelink.monolith.application.academic.command.student.approve;

import com.pfelink.monolith.domain.academic.entity.profile.StudentProfile;
import com.pfelink.monolith.domain.academic.enums.StudentStatus;
import com.pfelink.monolith.domain.academic.repository.IStudentProfileRepository;
import com.pfelink.monolith.infrastructure.event.events.faculty.StudentProfileApprovedEvent;
import com.pfelink.monolith.shared.cqrs.ICommandHandler;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApproveStudentProfileCommandHandler implements ICommandHandler<ApproveStudentProfileCommand, Result<Void>> {

    private final IStudentProfileRepository studentProfileRepository;
    private final com.pfelink.monolith.domain.auth.repository.IUserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public Result<Void> handle(ApproveStudentProfileCommand command) {
        StudentProfile profile = studentProfileRepository.findById(command.studentProfileId())
            .orElse(null);

        if (profile == null) {
            return Result.failure(Error.failure("Student.NotFound", "Student profile not found"));
        }

        // Update existing profile status
        profile.setStatus(StudentStatus.APPROVED);
        profile.setFacultyApproved(true);
        studentProfileRepository.save(profile);

        // Update the main User account status
        userRepository.findById(profile.getUserId()).ifPresent(user -> {
         
            user.setStatus(com.pfelink.monolith.domain.auth.enums.AccountStatus.ACTIVE);
            userRepository.save(user);
        });

        eventPublisher.publishEvent(StudentProfileApprovedEvent.of(
            profile.getUserId(),
            profile.getId(),
            profile.getFullName(),
            profile.getEmail(),
            profile.getFaculty() != null ? profile.getFaculty().getName() : "Unknown Faculty"
        ));

        return Result.success(null);
    }
}

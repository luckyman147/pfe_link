package com.pfelink.monolith.application.academic.command.student.reject;

import com.pfelink.monolith.domain.academic.entity.profile.StudentProfile;
import com.pfelink.monolith.domain.academic.enums.student.StudentStatus;
import com.pfelink.monolith.domain.academic.repository.IStudentProfileRepository;
import com.pfelink.monolith.shared.cqrs.ICommandHandler;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RejectStudentProfileCommandHandler implements ICommandHandler<RejectStudentProfileCommand, Result<Void>> {

    private final IStudentProfileRepository studentProfileRepository;

    @Override
    @Transactional
    public Result<Void> handle(RejectStudentProfileCommand command) {
        StudentProfile profile = studentProfileRepository.findById(command.studentProfileId())
            .orElse(null);

        if (profile == null) {
            return Result.failure(Error.failure("Student.NotFound", "Student profile not found"));
        }

        profile.setStatus(StudentStatus.REJECTED);
        profile.setFacultyApproved(false);
        studentProfileRepository.save(profile);

        return Result.success(null);
    }
}

package com.pfelink.monolith.application.academic.query.student.get_me;

import com.pfelink.monolith.application.academic.dto.response.StudentProfileResponse;
import com.pfelink.monolith.domain.academic.repository.IStudentProfileRepository;
import com.pfelink.monolith.shared.cqrs.IQueryHandler;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetMyStudentProfileQueryHandler implements IQueryHandler<GetMyStudentProfileQuery, Result<StudentProfileResponse>> {

    private final IStudentProfileRepository repository;

    @Override
    public Result<StudentProfileResponse> handle(GetMyStudentProfileQuery query) {
        return repository.findByUserId(query.userId())
            .map(profile -> Result.success(new StudentProfileResponse(
                profile.getId(),
                profile.getUserId(),
                profile.getFullName(),
                profile.getEmail(),
                profile.getStatus(),
                profile.getFaculty() != null ? profile.getFaculty().getId() : null
            )))
            .orElseGet(() -> Result.failure(new Error("Student.NotFound", "Student profile not found")));
    }
}

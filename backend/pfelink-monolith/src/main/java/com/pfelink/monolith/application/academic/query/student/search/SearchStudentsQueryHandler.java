package com.pfelink.monolith.application.academic.query.student.search;

import com.pfelink.monolith.domain.academic.entity.profile.StudentProfile;
import com.pfelink.monolith.domain.academic.repository.IStudentProfileRepository;
import com.pfelink.monolith.application.academic.dto.response.StudentProfileResponse;
import com.pfelink.monolith.shared.cqrs.IQueryHandler;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchStudentsQueryHandler implements IQueryHandler<SearchStudentsQuery, Result<List<StudentProfileResponse>>> {

    private final IStudentProfileRepository studentProfileRepository;

    @Override
    public Result<List<StudentProfileResponse>> handle(SearchStudentsQuery query) {
        List<StudentProfile> results = studentProfileRepository.findByEmailContainingIgnoreCase(
            query.email(),
            PageRequest.of(0, 5)
        );

        List<StudentProfileResponse> responses = results.stream()
            .map(profile -> new StudentProfileResponse(
                profile.getId(),
                profile.getUserId(),
                profile.getFullName(),
                profile.getEmail(),
                profile.getStatus(),
                profile.getFacultyId() != null ? profile.getFacultyId() : java.util.UUID.randomUUID()
            ))
            .toList();

        return Result.success(responses);
    }
}

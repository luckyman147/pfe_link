package com.pfelink.monolith.application.academic.query.advisor.get_by_faculty;

import com.pfelink.monolith.application.academic.dto.response.AdvisorProfileResponse;
import com.pfelink.monolith.domain.academic.enums.VerificationStatus;
import com.pfelink.monolith.domain.academic.repository.IAdvisorAssignmentRepository;
import com.pfelink.monolith.shared.cqrs.IQueryHandler;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetAdvisorsByFacultyQueryHandler implements IQueryHandler<GetAdvisorsByFacultyQuery, Result<List<AdvisorProfileResponse>>> {

    private final IAdvisorAssignmentRepository repository;

    @Override
    public Result<List<AdvisorProfileResponse>> handle(GetAdvisorsByFacultyQuery query) {
        List<AdvisorProfileResponse> advisors = repository.findByFacultyIdAndStatus(query.facultyId(), VerificationStatus.APPROVED)
            .stream()
            .map(assignment -> new AdvisorProfileResponse(
                assignment.getAdvisor().getId(),
                assignment.getAdvisor().getUserId(),
                assignment.getAdvisor().getFullName(),
                assignment.getAdvisor().getEmail(),
                assignment.getAdvisor().getTelephone(),
                assignment.getRole(), // Mapping role to department for now as requested by UI
                "" // specialization not yet in assignment/profile
            ))
            .collect(Collectors.toList());

        return Result.success(advisors);
    }
}

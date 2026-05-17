package com.pfelink.monolith.application.academic.query.selection.get_advisor_students;

import com.pfelink.monolith.domain.academic.entity.selection.SelectionRequest;
import com.pfelink.monolith.domain.academic.enums.project.SelectionStatus;
import com.pfelink.monolith.domain.academic.repository.ISelectionRequestRepository;
import com.pfelink.monolith.shared.cqrs.IQueryHandler;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAdvisorStudentsQueryHandler implements IQueryHandler<GetAdvisorStudentsQuery, Result<List<SelectionRequest>>> {

    private final ISelectionRequestRepository selectionRequestRepository;

    @Override
    public Result<List<SelectionRequest>> handle(GetAdvisorStudentsQuery query) {
        List<SelectionRequest> students = selectionRequestRepository.findByAdvisorUserIdAndFacultyIdAndStatus(
            query.advisorUserId(),
            query.facultyId(),
            SelectionStatus.APPROVED
        );
        return Result.success(students);
    }
}

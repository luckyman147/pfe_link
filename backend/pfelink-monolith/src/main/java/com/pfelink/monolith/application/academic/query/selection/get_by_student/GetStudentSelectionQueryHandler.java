package com.pfelink.monolith.application.academic.query.selection.get_by_student;

import com.pfelink.monolith.domain.academic.entity.selection.SelectionRequest;
import com.pfelink.monolith.domain.academic.repository.ISelectionRequestRepository;
import com.pfelink.monolith.shared.cqrs.IQueryHandler;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetStudentSelectionQueryHandler implements IQueryHandler<GetStudentSelectionQuery, Result<SelectionRequest>> {

    private final ISelectionRequestRepository selectionRequestRepository;

    @Override
    public Result<SelectionRequest> handle(GetStudentSelectionQuery query) {
        return selectionRequestRepository.findByStudentUserId(query.studentUserId())
            .map(Result::success)
            .orElseGet(() -> Result.failure(Error.failure("Selection.NotFound", "No selection request found for this student")));
    }
}

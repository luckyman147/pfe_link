package com.pfelink.monolith.application.academic.query.selection.get_by_student;

import com.pfelink.monolith.domain.academic.entity.selection.SelectionRequest;
import com.pfelink.monolith.shared.cqrs.IQuery;
import com.pfelink.monolith.shared.result.Result;
import java.util.UUID;

public record GetStudentSelectionQuery(
    UUID studentUserId
) implements IQuery<Result<SelectionRequest>> {}

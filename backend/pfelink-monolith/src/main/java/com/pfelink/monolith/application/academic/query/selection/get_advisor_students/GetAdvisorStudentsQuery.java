package com.pfelink.monolith.application.academic.query.selection.get_advisor_students;

import com.pfelink.monolith.domain.academic.entity.selection.SelectionRequest;
import com.pfelink.monolith.shared.cqrs.IQuery;
import com.pfelink.monolith.shared.result.Result;
import java.util.List;
import java.util.UUID;

public record GetAdvisorStudentsQuery(
    UUID advisorUserId,
    UUID facultyId
) implements IQuery<Result<List<SelectionRequest>>> {}

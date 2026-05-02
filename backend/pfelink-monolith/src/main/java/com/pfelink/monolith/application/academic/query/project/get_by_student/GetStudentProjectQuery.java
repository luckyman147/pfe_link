package com.pfelink.monolith.application.academic.query.project.get_by_student;

import com.pfelink.monolith.domain.academic.entity.project.Project;
import com.pfelink.monolith.shared.cqrs.IQuery;
import com.pfelink.monolith.shared.result.Result;
import java.util.UUID;

public record GetStudentProjectQuery(
    UUID studentUserId
) implements IQuery<Result<Project>> {}

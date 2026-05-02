package com.pfelink.monolith.application.academic.query.get_pending_faculties;

import com.pfelink.monolith.domain.academic.entity.faculty.PendingFaculty;
import com.pfelink.monolith.shared.cqrs.IQuery;
import com.pfelink.monolith.shared.result.Result;

import java.util.List;

public record GetPendingFacultiesQuery()
    implements IQuery<Result<List<PendingFaculty>>> {}

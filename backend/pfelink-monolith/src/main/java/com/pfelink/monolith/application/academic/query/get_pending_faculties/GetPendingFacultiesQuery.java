package com.pfelink.monolith.application.academic.query.get_pending_faculties;

import com.pfelink.monolith.domain.academic.entity.faculty.PendingFaculty;
import com.pfelink.monolith.shared.cqrs.IQuery;
import com.pfelink.monolith.shared.result.Result;
import org.springframework.data.domain.Page;

public record GetPendingFacultiesQuery(int page, int size)
    implements IQuery<Result<Page<PendingFaculty>>> {
    public GetPendingFacultiesQuery() {
        this(0, 20);
    }
}

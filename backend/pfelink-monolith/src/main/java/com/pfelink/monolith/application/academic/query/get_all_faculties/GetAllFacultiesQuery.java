package com.pfelink.monolith.application.academic.query.get_all_faculties;

import com.pfelink.monolith.application.academic.dto.response.FacultyResponse;
import com.pfelink.monolith.shared.cqrs.IQuery;
import com.pfelink.monolith.shared.result.Result;
import org.springframework.data.domain.Page;

public record GetAllFacultiesQuery(int page, int size) implements IQuery<Result<Page<FacultyResponse>>> {
    public GetAllFacultiesQuery() {
        this(0, 20);
    }
}

package com.pfelink.monolith.application.academic.query.get_pending_faculties;

import com.pfelink.monolith.domain.academic.entity.faculty.PendingFaculty;
import com.pfelink.monolith.domain.academic.repository.IPendingFacultyRepository;
import com.pfelink.monolith.shared.cqrs.IQueryHandler;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPendingFacultiesQueryHandler
        implements IQueryHandler<GetPendingFacultiesQuery, Result<List<PendingFaculty>>> {

    private final IPendingFacultyRepository pendingFacultyRepository;

    @Override
    public Result<List<PendingFaculty>> handle(GetPendingFacultiesQuery query) {
        return Result.success(pendingFacultyRepository.findAll());
    }
}

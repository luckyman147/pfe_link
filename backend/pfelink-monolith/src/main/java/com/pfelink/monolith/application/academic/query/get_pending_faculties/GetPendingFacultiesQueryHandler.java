package com.pfelink.monolith.application.academic.query.get_pending_faculties;

import com.pfelink.monolith.domain.academic.entity.faculty.PendingFaculty;
import com.pfelink.monolith.domain.academic.repository.IPendingFacultyRepository;
import com.pfelink.monolith.shared.cqrs.IQueryHandler;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetPendingFacultiesQueryHandler
        implements IQueryHandler<GetPendingFacultiesQuery, Result<Page<PendingFaculty>>> {

    private final IPendingFacultyRepository pendingFacultyRepository;

    @Override
    public Result<Page<PendingFaculty>> handle(GetPendingFacultiesQuery query) {
        Pageable pageable = PageRequest.of(query.page(), query.size(), Sort.by("createdAt").descending());
        return Result.success(pendingFacultyRepository.findAll(pageable));
    }
}

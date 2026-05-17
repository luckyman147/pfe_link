package com.pfelink.monolith.application.academic.query.get_all_faculties;

import com.pfelink.monolith.application.academic.dto.response.FacultyResponse;
import com.pfelink.monolith.domain.academic.repository.IFacultyRepository;
import com.pfelink.monolith.shared.cqrs.IQueryHandler;
import com.pfelink.monolith.shared.result.Result;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class GetAllFacultiesQueryHandler
        implements IQueryHandler<GetAllFacultiesQuery, Result<Page<FacultyResponse>>> {

    private final IFacultyRepository facultyRepository;

    public GetAllFacultiesQueryHandler(IFacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    @Cacheable(value = "faculties:page", key = "#q.page + ':' + #q.size", unless = "#result.body.isEmpty()")
    public Result<Page<FacultyResponse>> handle(GetAllFacultiesQuery q) {
        Pageable pageable = PageRequest.of(q.page(), q.size(), Sort.by("name").ascending());
        Page<FacultyResponse> faculties = facultyRepository.findAllValidated(pageable)
            .map(FacultyResponse::from);
        return Result.success(faculties);
    }
}

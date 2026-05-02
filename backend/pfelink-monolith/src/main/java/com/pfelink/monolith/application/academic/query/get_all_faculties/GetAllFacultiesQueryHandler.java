package com.pfelink.monolith.application.academic.query.get_all_faculties;

import com.pfelink.monolith.domain.academic.entity.faculty.Faculty;
import com.pfelink.monolith.domain.academic.repository.IFacultyRepository;
import com.pfelink.monolith.shared.cqrs.IQueryHandler;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllFacultiesQueryHandler
        implements IQueryHandler<GetAllFacultiesQuery, Result<List<Faculty>>> {

    private final IFacultyRepository facultyRepository;

    @Override
    public Result<List<Faculty>> handle(GetAllFacultiesQuery q) {
        return Result.success(facultyRepository.findAll());
    }
}

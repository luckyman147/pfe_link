package com.pfelink.monolith.application.academic.query.advisor.get_by_faculty;

import com.pfelink.monolith.application.academic.dto.response.AdvisorProfileResponse;
import com.pfelink.monolith.shared.cqrs.IQuery;
import com.pfelink.monolith.shared.result.Result;
import java.util.List;
import java.util.UUID;

public record GetAdvisorsByFacultyQuery(UUID facultyId) implements IQuery<Result<List<AdvisorProfileResponse>>> {}

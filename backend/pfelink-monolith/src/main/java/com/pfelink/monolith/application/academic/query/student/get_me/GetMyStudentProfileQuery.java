package com.pfelink.monolith.application.academic.query.student.get_me;

import com.pfelink.monolith.application.academic.dto.response.StudentProfileResponse;
import com.pfelink.monolith.shared.cqrs.IQuery;
import com.pfelink.monolith.shared.result.Result;
import java.util.UUID;

public record GetMyStudentProfileQuery(UUID userId) implements IQuery<Result<StudentProfileResponse>> {}

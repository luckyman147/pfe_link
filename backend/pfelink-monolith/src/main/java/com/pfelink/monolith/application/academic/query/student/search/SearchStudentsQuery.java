package com.pfelink.monolith.application.academic.query.student.search;

import com.pfelink.monolith.shared.cqrs.IQuery;
import com.pfelink.monolith.shared.result.Result;
import com.pfelink.monolith.application.academic.dto.response.StudentProfileResponse;
import java.util.List;

public record SearchStudentsQuery(String email) implements IQuery<Result<List<StudentProfileResponse>>> {}

package com.pfelink.monolith.application.academic.query.season.get_active;

import com.pfelink.monolith.domain.academic.entity.season.Season;
import com.pfelink.monolith.shared.cqrs.IQuery;
import com.pfelink.monolith.shared.result.Result;

public record GetActiveSeasonQuery() implements IQuery<Result<Season>> {}

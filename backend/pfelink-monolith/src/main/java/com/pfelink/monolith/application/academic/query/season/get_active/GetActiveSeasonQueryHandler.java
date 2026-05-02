package com.pfelink.monolith.application.academic.query.season.get_active;

import com.pfelink.monolith.domain.academic.entity.season.Season;
import com.pfelink.monolith.domain.academic.repository.ISeasonRepository;
import com.pfelink.monolith.shared.cqrs.IQueryHandler;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetActiveSeasonQueryHandler implements IQueryHandler<GetActiveSeasonQuery, Result<Season>> {

    private final ISeasonRepository seasonRepository;

    @Override
    public Result<Season> handle(GetActiveSeasonQuery query) {
        return seasonRepository.findActiveSeason()
            .map(Result::success)
            .orElseGet(() -> Result.failure(Error.failure("Season.NoActive", "No active academic season found")));
    }
}

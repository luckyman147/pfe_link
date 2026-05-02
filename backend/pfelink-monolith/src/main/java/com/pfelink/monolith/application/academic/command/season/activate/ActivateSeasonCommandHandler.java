package com.pfelink.monolith.application.academic.command.season.activate;

import com.pfelink.monolith.domain.academic.entity.season.Season;
import com.pfelink.monolith.domain.academic.repository.ISeasonRepository;
import com.pfelink.monolith.shared.cqrs.ICommandHandler;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ActivateSeasonCommandHandler implements ICommandHandler<ActivateSeasonCommand, Result<Void>> {

    private final ISeasonRepository seasonRepository;

    @Override
    @Transactional
    public Result<Void> handle(ActivateSeasonCommand cmd) {
        Season season = seasonRepository.findById(cmd.id()).orElse(null);
        if (season == null) {
            return Result.failure(Error.failure("Season.NotFound", "Season not found"));
        }

        seasonRepository.findActiveSeason().ifPresent(active -> {
            active.setActive(false);
            seasonRepository.save(active);
        });

        season.setActive(true);
        seasonRepository.save(season);

        return Result.success(null);
    }
}

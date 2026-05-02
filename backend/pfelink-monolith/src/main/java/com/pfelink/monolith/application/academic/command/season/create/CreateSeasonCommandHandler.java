package com.pfelink.monolith.application.academic.command.season.create;

import com.pfelink.monolith.domain.academic.entity.season.Season;
import com.pfelink.monolith.domain.academic.repository.ISeasonRepository;
import com.pfelink.monolith.shared.cqrs.ICommandHandler;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateSeasonCommandHandler implements ICommandHandler<CreateSeasonCommand, Result<UUID>> {

    private final ISeasonRepository seasonRepository;

    @Override
    @Transactional
    public Result<UUID> handle(CreateSeasonCommand cmd) {
        if (cmd.isActive()) {
            seasonRepository.findActiveSeason().ifPresent(s -> {
                s.setActive(false);
                seasonRepository.save(s);
            });
        }

        Season season = new Season();
        season.setName(cmd.name());
        season.setStartDate(cmd.startDate());
        season.setEndDate(cmd.endDate());
        season.setActive(cmd.isActive());

        Season saved = seasonRepository.save(season);
        return Result.success(saved.getId());
    }
}

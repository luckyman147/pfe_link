package com.pfelink.monolith.application.auth.command.approve_user;

import com.pfelink.monolith.domain.auth.entity.User;
import com.pfelink.monolith.domain.auth.enums.AccountStatus;
import com.pfelink.monolith.domain.auth.repository.IUserRepository;
import com.pfelink.monolith.infrastructure.event.events.auth.UserApprovedEvent;
import com.pfelink.monolith.shared.cqrs.ICommandHandler;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApproveUserCommandHandler
        implements ICommandHandler<ApproveUserCommand, Result<Void>> {

    private final IUserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public Result<Void> handle(ApproveUserCommand cmd) {
        User user = userRepository.findById(cmd.userId()).orElse(null);
        if (user == null) {
            return Result.failure(Error.failure("User.NotFound", "User not found"));
        }
        
        user.setStatus(AccountStatus.ACTIVE);
        userRepository.save(user);
        eventPublisher.publishEvent(new UserApprovedEvent(user.getEmail(), user.getFullName()));
        return Result.success(null);
    }
}

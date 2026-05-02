package com.pfelink.monolith.application.auth.command.verify_email;

import com.pfelink.monolith.domain.auth.entity.User;
import com.pfelink.monolith.domain.auth.enums.AccountStatus;
import com.pfelink.monolith.domain.auth.repository.IUserRepository;
import com.pfelink.monolith.infrastructure.event.events.auth.UserEmailVerifiedEvent;
import com.pfelink.monolith.shared.cqrs.ICommandHandler;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VerifyEmailCommandHandler
        implements ICommandHandler<VerifyEmailCommand, Result<Boolean>> {

    private final IUserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public Result<Boolean> handle(VerifyEmailCommand cmd) {
        User user = userRepository.findByEmailVerificationToken(cmd.token())
            .orElse(null);
        if (user == null) {
            return Result.failure(Error.failure("Auth.InvalidToken", "Invalid verification token"));
        }

        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        
        // Non-students are activated immediately upon email verification
        // Students remain PENDING until faculty approval
        if (!com.pfelink.monolith.domain.auth.enums.UserRole.STUDENT.equals(user.getRole())) {
            user.setStatus(AccountStatus.ACTIVE);
        }
        
        userRepository.save(user);

        eventPublisher.publishEvent(new UserEmailVerifiedEvent(
            user.getId(), user.getEmail(), user.getFullName(),
            user.getRole().name()
        ));

        return Result.success(true);
    }
}

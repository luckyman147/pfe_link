package com.pfelink.monolith.application.auth.command.register_student;

import com.pfelink.monolith.application.auth.dto.AuthResponseDTO;
import com.pfelink.monolith.domain.auth.entity.User;
import com.pfelink.monolith.domain.auth.enums.AccountStatus;
import com.pfelink.monolith.domain.auth.enums.UserRole;
import com.pfelink.monolith.domain.auth.repository.IUserRepository;
import com.pfelink.monolith.infrastructure.event.events.auth.UserSignedUpEvent;
import com.pfelink.monolith.infrastructure.security.service.OtpService;
import com.pfelink.monolith.infrastructure.event.events.auth.EmailVerificationEvent;
import com.pfelink.monolith.domain.storage.entity.DraftUpload;
import com.pfelink.monolith.domain.storage.repository.IDraftUploadRepository;
import com.pfelink.monolith.shared.cqrs.ICommandHandler;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterStudentCommandHandler
        implements ICommandHandler<RegisterStudentCommand, Result<AuthResponseDTO>> {

    private final IUserRepository userRepository;
    private final IDraftUploadRepository draftRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    private final OtpService otpService;

    @Override
    @Transactional
    public Result<AuthResponseDTO> handle(RegisterStudentCommand cmd) {
        if (userRepository.existsByEmail(cmd.email())) {
            return Result.failure(Error.failure("Auth.EmailExists", "Email already exists"));
        }

        String actualCardUrl = cmd.studentCardUrl();
        if (cmd.draftId() != null && !cmd.draftId().isBlank()) {
            DraftUpload draft = draftRepository.findById(cmd.draftId()).orElse(null);
            if (draft != null) {
                actualCardUrl = draft.getUrl();
                draft.setCommitted(true);
                draftRepository.save(draft);
            }
        }

        User user = new User();
        user.setEmail(cmd.email());
        user.setPassword(passwordEncoder.encode(cmd.password()));
        user.setFullName(cmd.fullName());
        user.setTelephone(cmd.telephone());
        user.setRole(UserRole.STUDENT);
        user.setStatus(AccountStatus.PENDING);
   

        String token = otpService.generateEmailVerificationToken();
        user.setEmailVerificationToken(token);
        User saved = userRepository.save(user);

        eventPublisher.publishEvent( UserSignedUpEvent.of(
            saved.getId(), saved.getEmail(), saved.getFullName(), saved.getRole().name(),
            cmd.cinNumber(), null, actualCardUrl, cmd.facultyId()
        ));

        eventPublisher.publishEvent( EmailVerificationEvent.of(saved.getEmail(), saved.getFullName(), token));

        return Result.success( AuthResponseDTO.of(
            saved.getId(), saved.getEmail(), saved.getFullName(),
            saved.getRole(), saved.getStatus(), null, null
        ));
    }
}

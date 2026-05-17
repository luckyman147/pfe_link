package com.pfelink.monolith.application.auth.command.register_advisor;

import com.pfelink.monolith.application.auth.dto.AuthResponseDTO;
import com.pfelink.monolith.domain.academic.entity.faculty.Faculty;
import com.pfelink.monolith.domain.academic.repository.IFacultyRepository;
import com.pfelink.monolith.domain.auth.entity.User;
import com.pfelink.monolith.domain.auth.enums.AccountStatus;
import com.pfelink.monolith.domain.auth.enums.UserRole;
import com.pfelink.monolith.domain.auth.repository.IUserRepository;
import com.pfelink.monolith.infrastructure.event.events.auth.EmailVerificationEvent;
import com.pfelink.monolith.infrastructure.event.events.auth.UserSignedUpEvent;
import com.pfelink.monolith.infrastructure.security.service.OtpService;
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

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterAdvisorCommandHandler
        implements ICommandHandler<RegisterAdvisorCommand, Result<AuthResponseDTO>> {

    private final IUserRepository userRepository;
    private final IDraftUploadRepository draftRepository;
    private final IFacultyRepository facultyRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    private final OtpService otpService;

    @Override
    @Transactional
    public Result<AuthResponseDTO> handle(RegisterAdvisorCommand cmd) {
        if (userRepository.existsByEmail(cmd.email())) {
            return Result.failure(Error.failure("Auth.EmailExists", "Email already exists"));
        }

        String actualCinCardUrl = resolveDraftUrl(cmd.draftId(), cmd.cinCardUrl());
        FacultyRef ref = resolveFacultyRef(cmd.facultyId(), cmd.facultyDomainEmail());

        User user = new User();
        user.setEmail(cmd.email());
        user.setPassword(passwordEncoder.encode(cmd.password()));
        user.setFullName(cmd.fullName());
        user.setTelephone(cmd.telephone());
        user.setRole(UserRole.ADVISOR);
        user.setStatus(AccountStatus.PENDING);

        String token = otpService.generateEmailVerificationToken();
        user.setEmailVerificationToken(token);
        User saved = userRepository.save(user);

        eventPublisher.publishEvent(new UserSignedUpEvent(
            saved.getId(), saved.getEmail(),
            saved.getFullName(), saved.getRole().name(),
            cmd.cinNumber(), actualCinCardUrl, null,
            ref.resolvedFacultyId() == null ? null : ref.resolvedFacultyId().toString(),
            ref.pendingEmail()
        ));
        eventPublisher.publishEvent(new EmailVerificationEvent(
            saved.getEmail(), saved.getFullName(), token
        ));

        return Result.success(new AuthResponseDTO(
            saved.getId(), saved.getEmail(), saved.getFullName(),
            saved.getRole(), saved.getStatus(), null, null
        ));
    }

    private String resolveDraftUrl(String draftId, String fallbackUrl) {
        if (draftId == null || draftId.isBlank()) return fallbackUrl;
        DraftUpload draft = draftRepository.findById(draftId).orElse(null);
        if (draft == null) return fallbackUrl;
        draft.setCommitted(true);
        draftRepository.save(draft);
        return draft.getUrl();
    }

    private FacultyRef resolveFacultyRef(UUID facultyId, String facultyDomainEmail) {
        if (facultyId != null) return new FacultyRef(facultyId, null);
        if (facultyDomainEmail == null || facultyDomainEmail.isBlank()) return new FacultyRef(null, null);
        UUID resolved = facultyRepository.findByEmail(facultyDomainEmail).map(Faculty::getId).orElse(null);
        return new FacultyRef(resolved, resolved == null ? facultyDomainEmail : null);
    }

    private record FacultyRef(UUID resolvedFacultyId, String pendingEmail) {}
}

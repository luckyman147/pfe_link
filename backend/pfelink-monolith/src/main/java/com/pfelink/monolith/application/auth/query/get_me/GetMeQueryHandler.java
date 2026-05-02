package com.pfelink.monolith.application.auth.query.get_me;

import com.pfelink.monolith.application.auth.dto.UserDTO;
import com.pfelink.monolith.domain.academic.repository.IAdvisorProfileRepository;
import com.pfelink.monolith.domain.academic.repository.IStudentProfileRepository;
import com.pfelink.monolith.domain.auth.entity.User;
import com.pfelink.monolith.domain.auth.enums.UserRole;
import com.pfelink.monolith.domain.auth.repository.IUserRepository;
import com.pfelink.monolith.shared.cqrs.IQueryHandler;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetMeQueryHandler implements IQueryHandler<GetMeQuery, Result<UserDTO>> {

    private final IUserRepository userRepository;
    private final IStudentProfileRepository studentProfileRepository;
    private final IAdvisorProfileRepository advisorProfileRepository;

    @Override
    public Result<UserDTO> handle(GetMeQuery query) {
        return userRepository.findById(query.userId())
                .map(this::mapToDTO)
                .orElseGet(() -> Result.failure(Error.notFound("User not found")));
    }

    private Result<UserDTO> mapToDTO(User user) {
        UserDTO.StudentProfileDTO studentDTO = null;
        UserDTO.AdvisorProfileDTO advisorDTO = null;

        if (user.getRole() == UserRole.STUDENT) {
            studentDTO = studentProfileRepository.findByUserId(user.getId())
                    .map(p -> new UserDTO.StudentProfileDTO(
                            p.getId(),
                            p.getFullName(),
                            p.getStudentCardUrl(),
                            p.getFaculty() != null ? p.getFaculty().getName() : null,
                            (p.getFaculty() != null && p.getFaculty().getAddress() != null) ? p.getFaculty().getAddress().getCity() : null,
                            p.getStatus().name()
                    )).orElse(null);
        } else if (user.getRole() == UserRole.ADVISOR) {
            advisorDTO = advisorProfileRepository.findByUserId(user.getId())
                    .map(p -> new UserDTO.AdvisorProfileDTO(
                            p.getId(),
                            p.getFullName(),
                            p.getTelephone(),
                            null, // specialization not in entity yet?
                            null  // department not in entity yet?
                    )).orElse(null);
        }

        return Result.success(new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                user.getStatus(),
                user.isEmailVerified(),
                user.getImageUrl(),
                user.getTelephone(),
                studentDTO,
                advisorDTO
        ));
    }
}

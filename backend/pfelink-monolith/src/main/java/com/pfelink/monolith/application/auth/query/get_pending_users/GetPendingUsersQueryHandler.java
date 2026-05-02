package com.pfelink.monolith.application.auth.query.get_pending_users;

import com.pfelink.monolith.application.auth.dto.UserDTO;
import com.pfelink.monolith.domain.auth.entity.User;
import com.pfelink.monolith.domain.auth.repository.IUserRepository;
import com.pfelink.monolith.shared.cqrs.IQueryHandler;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPendingUsersQueryHandler
        implements IQueryHandler<GetPendingUsersQuery, Result<List<UserDTO>>> {

    private final IUserRepository userRepository;

    @Override
    public Result<List<UserDTO>> handle(GetPendingUsersQuery q) {
        List<User> users = userRepository.findByRole(q.role());
        List<UserDTO> dtos = users.stream()
            .map(u -> new UserDTO(u.getId(), u.getEmail(), u.getFullName(),
                    u.getRole(), u.getStatus(), u.isEmailVerified(),
                    null, null, null, null))
            .toList();

        return Result.success(dtos);
    }
}

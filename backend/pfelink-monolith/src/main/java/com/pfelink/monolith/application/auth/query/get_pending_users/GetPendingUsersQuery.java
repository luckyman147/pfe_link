package com.pfelink.monolith.application.auth.query.get_pending_users;

import com.pfelink.monolith.application.auth.dto.UserDTO;
import com.pfelink.monolith.domain.auth.enums.UserRole;
import com.pfelink.monolith.shared.cqrs.IQuery;
import com.pfelink.monolith.shared.result.Result;

import java.util.List;

public record GetPendingUsersQuery(UserRole role) implements IQuery<Result<List<UserDTO>>> {}

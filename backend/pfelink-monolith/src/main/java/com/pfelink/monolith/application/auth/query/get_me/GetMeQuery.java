package com.pfelink.monolith.application.auth.query.get_me;

import com.pfelink.monolith.application.auth.dto.UserDTO;
import com.pfelink.monolith.shared.cqrs.IQuery;
import com.pfelink.monolith.shared.result.Result;

import java.util.UUID;

public record GetMeQuery(UUID userId) implements IQuery<Result<UserDTO>> {}

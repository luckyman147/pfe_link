package com.pfelink.monolith.application.academic.command.student.approve;

import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;
import java.util.UUID;

public record ApproveStudentProfileCommand(
    UUID studentProfileId
) implements ICommand<Result<Void>> {}

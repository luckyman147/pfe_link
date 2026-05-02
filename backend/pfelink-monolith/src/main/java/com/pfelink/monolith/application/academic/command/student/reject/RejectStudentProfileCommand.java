package com.pfelink.monolith.application.academic.command.student.reject;

import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;
import java.util.UUID;

public record RejectStudentProfileCommand(
    UUID studentProfileId
) implements ICommand<Result<Void>> {}

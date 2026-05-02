package com.pfelink.monolith.application.auth.command.register_student;

import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;
import com.pfelink.monolith.application.auth.dto.AuthResponseDTO;

public record RegisterStudentCommand(
    String email,
    String password,
    String fullName,
    String telephone,
    String cinNumber,

    String studentCardUrl,
    String draftId,
    String facultyId
) implements ICommand<Result<AuthResponseDTO>> {
    public static RegisterStudentCommand of(String email, String password, String fullName, String telephone, String cinNumber, String studentCardUrl, String draftId, String facultyId) {
        return new RegisterStudentCommand(email, password, fullName, telephone, cinNumber, studentCardUrl, draftId, facultyId);
    }
}

package com.pfelink.monolith.application.academic.command.create_faculty;

import com.pfelink.monolith.shared.cqrs.ICommand;
import com.pfelink.monolith.shared.result.Result;
import java.util.UUID;

public record CreateFacultyCommand(
    String name,
    String abbreviation,
    String email,
    String websiteUrl,
    String imageUrl,
    String adminPassword,
    
    // Simplified Address Path
    String path
) implements ICommand<Result<UUID>> {}

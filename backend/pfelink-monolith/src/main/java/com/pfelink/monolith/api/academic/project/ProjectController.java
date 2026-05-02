package com.pfelink.monolith.api.academic.project;

import com.pfelink.monolith.api.academic.project.dto.CreateProjectRequest;
import com.pfelink.monolith.api.academic.project.dto.InviteFriendRequest;
import com.pfelink.monolith.application.academic.command.project.accept_invitation.AcceptProjectInvitationCommand;
import com.pfelink.monolith.application.academic.command.project.create.CreateProjectCommand;
import com.pfelink.monolith.application.academic.command.project.invite_friend.InviteFriendCommand;
import com.pfelink.monolith.application.academic.query.project.get_by_student.GetStudentProjectQuery;
import com.pfelink.monolith.shared.cqrs.Dispatcher;
import com.pfelink.monolith.infrastructure.api.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Tag(name = "Project Management", description = "Endpoints for students to manage projects and group members")
public class ProjectController {

    private final Dispatcher dispatcher;

    @PostMapping
    @Operation(summary = "Create a new project (Student)")
    public ResponseEntity<?> createProject(@RequestBody CreateProjectRequest request) {
        return ResponseUtil.toResponse(dispatcher.send(new CreateProjectCommand(
            com.pfelink.monolith.infrastructure.security.util.SecurityUtils.getCurrentUserId(),
            request.title(),
            request.description()
        )));
    }

    @PostMapping("/invite")
    @Operation(summary = "Invite a friend to a project (Student Owner)")
    public ResponseEntity<?> inviteFriend(@RequestBody InviteFriendRequest request) {
        return ResponseUtil.toResponse(dispatcher.send(new InviteFriendCommand(
            com.pfelink.monolith.infrastructure.security.util.SecurityUtils.getCurrentUserId(),
            request.projectId(),
            request.inviteeId()
        )));
    }

    @PostMapping("/invitations/{id}/accept")
    @Operation(summary = "Accept a project invitation (Invited Student)")
    public ResponseEntity<?> acceptInvitation(@PathVariable UUID id) {
        return ResponseUtil.toResponse(dispatcher.send(new AcceptProjectInvitationCommand(id)));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current authenticated student's project")
    public ResponseEntity<?> getMyProject() {
        return ResponseUtil.toResponse(dispatcher.query(new GetStudentProjectQuery(
            com.pfelink.monolith.infrastructure.security.util.SecurityUtils.getCurrentUserId()
        )));
    }
}

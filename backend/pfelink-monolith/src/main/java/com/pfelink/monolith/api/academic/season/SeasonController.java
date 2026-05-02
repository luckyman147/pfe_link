package com.pfelink.monolith.api.academic.season;

import com.pfelink.monolith.infrastructure.api.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/seasons")
@RequiredArgsConstructor
@Tag(name = "Season Management", description = "Admin only operations for seasons")
public class SeasonController {

    private final com.pfelink.monolith.shared.cqrs.Dispatcher dispatcher;

    @PostMapping
    @Operation(summary = "Create a new season")
    public ResponseEntity<?> createSeason(@RequestBody com.pfelink.monolith.application.academic.command.season.create.CreateSeasonCommand command) {
        return ResponseUtil.toResponse(dispatcher.send(command));
    }

    @GetMapping("/active")
    @Operation(summary = "Get the active season")
    public ResponseEntity<?> getActiveSeason() {
        return ResponseUtil.toResponse(dispatcher.query(new com.pfelink.monolith.application.academic.query.season.get_active.GetActiveSeasonQuery()));
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate a specific season")
    public ResponseEntity<?> activateSeason(@PathVariable java.util.UUID id) {
        return ResponseUtil.toResponse(dispatcher.send(new com.pfelink.monolith.application.academic.command.season.activate.ActivateSeasonCommand(id)));
    }
}

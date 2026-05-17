package com.pfelink.monolith.application.academic.command.selection.reject;

import com.pfelink.monolith.domain.academic.entity.selection.SelectionRequest;
import com.pfelink.monolith.domain.academic.enums.project.SelectionStatus;
import com.pfelink.monolith.domain.academic.repository.ISelectionRequestRepository;
import com.pfelink.monolith.infrastructure.event.events.selection.SelectionRequestRespondedEvent;
import com.pfelink.monolith.shared.cqrs.ICommandHandler;
import com.pfelink.monolith.shared.result.Error;
import com.pfelink.monolith.shared.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RejectSelectionRequestCommandHandler implements ICommandHandler<RejectSelectionRequestCommand, Result<Void>> {

    private final ISelectionRequestRepository selectionRequestRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public Result<Void> handle(RejectSelectionRequestCommand cmd) {
        SelectionRequest request = selectionRequestRepository.findById(cmd.requestId()).orElse(null);
        if (request == null) {
            return Result.failure(Error.failure("Selection.NotFound", "Selection request not found"));
        }

        if (request.getStatus() != SelectionStatus.PENDING) {
            return Result.failure(Error.failure("Selection.InvalidStatus", "Request is already " + request.getStatus()));
        }

        request.setStatus(SelectionStatus.REJECTED);
        request.setRespondedAt(LocalDateTime.now());
        selectionRequestRepository.save(request);

        eventPublisher.publishEvent(new SelectionRequestRespondedEvent(LocalDateTime.now(), request));

        return Result.success(null);
    }
}

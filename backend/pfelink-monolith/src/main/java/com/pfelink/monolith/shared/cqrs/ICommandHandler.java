package com.pfelink.monolith.shared.cqrs;

public interface ICommandHandler<C extends ICommand<R>, R> {
    R handle(C command);
}

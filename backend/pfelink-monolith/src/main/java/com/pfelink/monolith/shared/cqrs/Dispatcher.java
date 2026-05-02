package com.pfelink.monolith.shared.cqrs;

public interface Dispatcher {
    <R> R send(ICommand<R> command);
    <R> R query(IQuery<R> query);
}

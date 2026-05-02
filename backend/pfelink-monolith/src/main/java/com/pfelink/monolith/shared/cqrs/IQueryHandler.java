package com.pfelink.monolith.shared.cqrs;

public interface IQueryHandler<Q extends IQuery<R>, R> {
    R handle(Q query);
}

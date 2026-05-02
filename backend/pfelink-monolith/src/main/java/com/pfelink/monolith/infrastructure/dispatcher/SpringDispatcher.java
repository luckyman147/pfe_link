package com.pfelink.monolith.infrastructure.dispatcher;

import com.pfelink.monolith.shared.cqrs.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SpringDispatcher implements Dispatcher, SmartInitializingSingleton {

    private final ApplicationContext applicationContext;
    private final Map<Class<?>, ICommandHandler<?, ?>> commandHandlers = new ConcurrentHashMap<>();
    private final Map<Class<?>, IQueryHandler<?, ?>> queryHandlers = new ConcurrentHashMap<>();

    public SpringDispatcher(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {
        registerAll(ICommandHandler.class, commandHandlers);
        registerAll(IQueryHandler.class, queryHandlers);
    }

    @SuppressWarnings("unchecked")
    private void registerAll(Class<?> handlerInterface, Map<Class<?>, ?> targetMap) {
        applicationContext.getBeansOfType(handlerInterface).values().forEach(handler -> {
            Class<?> targetClass = AopProxyUtils.ultimateTargetClass(handler);
            Class<?> type = ResolvableType.forClass(targetClass)
                    .as(handlerInterface)
                    .getGeneric(0)
                    .resolve();
            if (type != null) {
                ((Map<Class<?>, Object>) targetMap).put(type, handler);
                log.info("Registered {}: {} -> {}", 
                    handlerInterface.getSimpleName(), 
                    type.getSimpleName(), 
                    targetClass.getSimpleName());
            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> R send(ICommand<R> command) {
        ICommandHandler<ICommand<R>, R> handler = 
                (ICommandHandler<ICommand<R>, R>) commandHandlers.get(command.getClass());
        if (handler == null) {
            throw new HandlerNotFoundException(command.getClass());
        }
        return handler.handle(command);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> R query(IQuery<R> query) {
        IQueryHandler<IQuery<R>, R> handler = 
                (IQueryHandler<IQuery<R>, R>) queryHandlers.get(query.getClass());
        if (handler == null) {
            throw new HandlerNotFoundException(query.getClass());
        }
        return handler.handle(query);
    }
}

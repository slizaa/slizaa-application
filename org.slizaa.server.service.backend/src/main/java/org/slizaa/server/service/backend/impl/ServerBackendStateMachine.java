package org.slizaa.server.service.backend.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

import static org.slizaa.server.service.backend.impl.ServerBackendStateMachine.Events.*;
import static org.slizaa.server.service.backend.impl.ServerBackendStateMachine.States.*;

@Configuration
@EnableStateMachine
public class ServerBackendStateMachine
        extends EnumStateMachineConfigurerAdapter<ServerBackendStateMachine.States, ServerBackendStateMachine.Events> {

    /**
     *
     */
    public static enum States {
        INITIAL, CHOICE, BACKEND_UNCONFIGURED, BACKEND_CONFIGURED, UPDATING_BACKEND_CONFIGURATION
    }

    /**
     *
     */
    public static enum Events {
        UPDATE_BACKEND_CONFIGURATION, UPDATE_FAILED, UPDATE_SUCCEDED
    }

    @Autowired
    private IServerBackendStateMachineContext _context;

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states)
            throws Exception {

        //
        states
                .withStates()
                .initial(States.INITIAL)
                .choice(States.CHOICE)
                .states(EnumSet.allOf(States.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
            throws Exception {

        //
        transitions
                //
                .withExternal()
                .source(INITIAL)
                .target(CHOICE)
                .and()
                //
                .withChoice()
                .source(CHOICE)
                .first(BACKEND_CONFIGURED, ctx -> _context.canConfigureBackend(), ctx -> _context.configureBackend())
                .last(BACKEND_UNCONFIGURED, ctx -> _context.unconfigureBackend())
                .and()
                //
                .withExternal()
                .source(BACKEND_CONFIGURED)
                .target(UPDATING_BACKEND_CONFIGURATION)
                .event(UPDATE_BACKEND_CONFIGURATION)
                .action(ctx -> _context.updateBackendConfiguration())
                .and()
                //
                .withExternal()
                .source(BACKEND_UNCONFIGURED)
                .target(UPDATING_BACKEND_CONFIGURATION)
                .event(UPDATE_BACKEND_CONFIGURATION)
                .action(ctx -> {
                    if (_context.updateBackendConfiguration()) {
                        ctx.getStateMachine().sendEvent(MessageBuilder
                                .withPayload(Events.UPDATE_SUCCEDED)
                                .build());
                    } else {
                        ctx.getStateMachine().sendEvent(MessageBuilder
                                .withPayload(Events.UPDATE_FAILED)
                                .build());
                    }
                })
                .and()
                //
                .withExternal()
                .source(UPDATING_BACKEND_CONFIGURATION)
                .target(BACKEND_UNCONFIGURED)
                .event(UPDATE_FAILED)
                .action(ctx -> _context.unconfigureBackend())
                .and()
                //
                .withExternal()
                .source(UPDATING_BACKEND_CONFIGURATION)
                .target(BACKEND_CONFIGURED)
                .action(ctx -> _context.configureBackend())
                .event(UPDATE_SUCCEDED);
    }


}

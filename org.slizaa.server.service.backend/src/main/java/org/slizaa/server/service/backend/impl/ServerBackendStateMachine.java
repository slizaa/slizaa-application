package org.slizaa.server.service.backend.impl;

import static org.slizaa.server.service.backend.impl.ServerBackendStateMachine.Events.UPDATE_BACKEND_CONFIGURATION;
import static org.slizaa.server.service.backend.impl.ServerBackendStateMachine.Events.UPDATE_FAILED;
import static org.slizaa.server.service.backend.impl.ServerBackendStateMachine.Events.UPDATE_SUCCEDED;
import static org.slizaa.server.service.backend.impl.ServerBackendStateMachine.States.BACKEND_CONFIGURED;
import static org.slizaa.server.service.backend.impl.ServerBackendStateMachine.States.BACKEND_UNCONFIGURED;
import static org.slizaa.server.service.backend.impl.ServerBackendStateMachine.States.CHOICE;
import static org.slizaa.server.service.backend.impl.ServerBackendStateMachine.States.INITIAL;
import static org.slizaa.server.service.backend.impl.ServerBackendStateMachine.States.UPDATING_BACKEND_CONFIGURATION;

import java.util.EnumSet;
import java.util.List;

import org.slizaa.server.service.extensions.IExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachine
public class ServerBackendStateMachine
        extends EnumStateMachineConfigurerAdapter<ServerBackendStateMachine.States, ServerBackendStateMachine.Events> {

    public static final String HEADER_EXTENSIONS_TO_INSTALL = "HEADER_EXTENSIONS_TO_INSTALL";

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
        UPDATE_BACKEND_CONFIGURATION, CLEAR_BACKEND_CONFIGURATION, UPDATE_FAILED, UPDATE_SUCCEDED
    }

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
	                .first(BACKEND_CONFIGURED, ctx -> context().get().canConfigureBackend(), ctx -> context().get().configureBackend())
	                .last(BACKEND_UNCONFIGURED, ctx -> context().get().unconfigureBackend())
	                .and()
                //
                .withExternal()
	                .source(BACKEND_CONFIGURED)
	                .target(UPDATING_BACKEND_CONFIGURATION)
	                .event(UPDATE_BACKEND_CONFIGURATION)
	                .action(ctx -> {
	                	List<IExtension> extensions = (List<IExtension>) ctx.getMessageHeader(HEADER_EXTENSIONS_TO_INSTALL);
	                	context().get().updateBackendConfiguration(extensions);
	                })
	                .and()
                //
                .withExternal()
	                .source(BACKEND_UNCONFIGURED)
	                .target(UPDATING_BACKEND_CONFIGURATION)
	                .event(UPDATE_BACKEND_CONFIGURATION)
	                .action(ctx -> {
	                	List<IExtension> extensions = (List<IExtension>) ctx.getMessageHeader(HEADER_EXTENSIONS_TO_INSTALL);
	                    if (context().get().updateBackendConfiguration(extensions)) {
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
	                .action(ctx -> context().get().unconfigureBackend())
	                .and()
                //
                .withExternal()
	                .source(UPDATING_BACKEND_CONFIGURATION)
	                .target(BACKEND_CONFIGURED)
	                .action(ctx -> context().get().configureBackend())
	                .event(UPDATE_SUCCEDED)
	                .and()
                //
                .withExternal()
	                .source(BACKEND_CONFIGURED)
	                .target(BACKEND_UNCONFIGURED)
	                .action(ctx -> context().get().unconfigureBackend())
	                .event(Events.CLEAR_BACKEND_CONFIGURATION);
    }

    @Bean
    public ContextHolder context() {
        return new ContextHolder();
    }

    public static class ContextHolder {

        @Autowired
        private IServerBackendStateMachineContext _context;

        public IServerBackendStateMachineContext get() {
            return _context;
        }
    }
}

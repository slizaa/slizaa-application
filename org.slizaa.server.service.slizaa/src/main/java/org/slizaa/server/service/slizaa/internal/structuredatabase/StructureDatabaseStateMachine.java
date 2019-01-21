package org.slizaa.server.service.slizaa.internal.structuredatabase;

import java.util.EnumSet;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;

@EnableStateMachineFactory
public class StructureDatabaseStateMachine
		extends EnumStateMachineConfigurerAdapter<StructureDatabaseState, StructureDatabaseEvent> {

	@Autowired
	private StructureDatabaseFactory _structureDatabaseFactory;

	@Override
	public void configure(StateMachineConfigurationConfigurer<StructureDatabaseState, StructureDatabaseEvent> config)
			throws Exception {

		config.withConfiguration().autoStartup(false);
	}

	@Override
	public void configure(StateMachineStateConfigurer<StructureDatabaseState, StructureDatabaseEvent> states)
			throws Exception {

		states
		// @formatter:off
			.withStates()
				.initial(StructureDatabaseState.INITIAL)
				.choice(StructureDatabaseState.PARSING)
				.states(EnumSet.allOf(StructureDatabaseState.class));
		// @formatter:on
	}

	@Override
	public void configure(StateMachineTransitionConfigurer<StructureDatabaseState, StructureDatabaseEvent> transitions)
			throws Exception {

		transitions
		// @formatter:off
			.withExternal()
				.source(StructureDatabaseState.INITIAL)
				.target(StructureDatabaseState.NOT_RUNNING)
				.guard(guardWithCtx(ctx -> ctx.hasPopulatedDatabaseDirectory()))
				.and()
			.withExternal()
				.source(StructureDatabaseState.INITIAL)
				.target(StructureDatabaseState.PARSING)
				.event(StructureDatabaseEvent.PARSE)
				.and()
			.withChoice()
				.source(StructureDatabaseState.PARSING)
				.first(StructureDatabaseState.RUNNING, guardWithCtx(ctx -> ctx.isRunning()))
				.last(StructureDatabaseState.NOT_RUNNING);
			// @formatter:on
	}

	private Action<StructureDatabaseState, StructureDatabaseEvent> actionWithCtx(
			Consumer<StructureDatabaseStateMachineContext> consumer) {

		return new Action<StructureDatabaseState, StructureDatabaseEvent>() {
			@Override
			public void execute(StateContext<StructureDatabaseState, StructureDatabaseEvent> context) {
				consumer.accept(_structureDatabaseFactory.context(context.getStateMachine()));
			}
		};
	}

	private Guard<StructureDatabaseState, StructureDatabaseEvent> guardWithCtx(
			Function<StructureDatabaseStateMachineContext, Boolean> guard) {

		return new Guard<StructureDatabaseState, StructureDatabaseEvent>() {
			@Override
			public boolean evaluate(StateContext<StructureDatabaseState, StructureDatabaseEvent> context) {
				return guard.apply(_structureDatabaseFactory.context(context.getStateMachine()));
			}
		};

	}
}

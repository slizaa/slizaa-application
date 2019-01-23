package org.slizaa.server.service.slizaa.internal.structuredatabase;

import java.util.EnumSet;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SyncTaskExecutor;
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
		extends EnumStateMachineConfigurerAdapter<StructureDatabaseState, StructureDatabaseTrigger> {

	@Autowired
	private StructureDatabaseFactory _structureDatabaseFactory;

	@Override
	public void configure(StateMachineConfigurationConfigurer<StructureDatabaseState, StructureDatabaseTrigger> config)
			throws Exception {

		config
			.withConfiguration()
			.taskExecutor(new SyncTaskExecutor())
			.autoStartup(false);
	}

	@Override
	public void configure(StateMachineStateConfigurer<StructureDatabaseState, StructureDatabaseTrigger> states)
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
	public void configure(StateMachineTransitionConfigurer<StructureDatabaseState, StructureDatabaseTrigger> transitions)
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
				.event(StructureDatabaseTrigger.PARSE)
				.action(actionWithCtx(ctx -> ctx.parse()))
				.and()
			.withChoice()
				.source(StructureDatabaseState.PARSING)
				.first(StructureDatabaseState.RUNNING, guardWithCtx(ctx -> ctx.isRunning()))
				.last(StructureDatabaseState.NOT_RUNNING)
				.and()
			.withExternal()
				.source(StructureDatabaseState.NOT_RUNNING)
				.target(StructureDatabaseState.PARSING)
				.event(StructureDatabaseTrigger.PARSE)
				.action(actionWithCtx(ctx -> ctx.parse()))
				.and()
			.withExternal()
				.source(StructureDatabaseState.RUNNING)
				.target(StructureDatabaseState.NOT_RUNNING)
				.event(StructureDatabaseTrigger.STOP)
				.action(actionWithCtx(ctx -> ctx.stop()))
			.and()
				.withExternal()
				.source(StructureDatabaseState.NOT_RUNNING)
				.target(StructureDatabaseState.TERMINATED)
				.event(StructureDatabaseTrigger.TERMINATE)
				.action(actionWithCtx(ctx -> ctx.terminate()))
			// TERMINATE
			.and()
				.withExternal()
				.source(StructureDatabaseState.INITIAL)
				.target(StructureDatabaseState.TERMINATED)
				.event(StructureDatabaseTrigger.TERMINATE)
				.action(actionWithCtx(ctx -> ctx.terminate()))
			.and()
				.withExternal()
				.source(StructureDatabaseState.CONFIGURED)
				.target(StructureDatabaseState.TERMINATED)
				.event(StructureDatabaseTrigger.TERMINATE)
				.action(actionWithCtx(ctx -> ctx.terminate()))
			.and()
				.withExternal()
				.source(StructureDatabaseState.RUNNING)
				.target(StructureDatabaseState.TERMINATED)
				.event(StructureDatabaseTrigger.TERMINATE)
				.action(actionWithCtx(ctx -> ctx.terminate()))
			.and()
				.withExternal()
				.source(StructureDatabaseState.NOT_RUNNING)
				.target(StructureDatabaseState.TERMINATED)
				.event(StructureDatabaseTrigger.TERMINATE)
				.action(actionWithCtx(ctx -> ctx.terminate()))
		;
			// @formatter:on
	}

	private Action<StructureDatabaseState, StructureDatabaseTrigger> actionWithCtx(
			Consumer<StructureDatabaseStateMachineContext> consumer) {

		return new Action<StructureDatabaseState, StructureDatabaseTrigger>() {
			@Override
			public void execute(StateContext<StructureDatabaseState, StructureDatabaseTrigger> context) {
				consumer.accept(_structureDatabaseFactory.context(context.getStateMachine()));
			}
		};
	}

	private Guard<StructureDatabaseState, StructureDatabaseTrigger> guardWithCtx(
			Function<StructureDatabaseStateMachineContext, Boolean> guard) {

		return new Guard<StructureDatabaseState, StructureDatabaseTrigger>() {
			@Override
			public boolean evaluate(StateContext<StructureDatabaseState, StructureDatabaseTrigger> context) {
				return guard.apply(_structureDatabaseFactory.context(context.getStateMachine()));
			}
		};

	}
}

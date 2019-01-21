package org.slizaa.server.service.slizaa.internal.structuredatabase;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.transition.Transition;

@Configuration
@EnableStateMachineFactory(name = "org.slizaa.server.service.slizaa.internal.structuredatabase.StructureDatabaseStateMachine")
@WithStateMachine(id = "org.slizaa.server.service.slizaa.internal.structuredatabase.StructureDatabaseStateMachine")
public class StructureDatabaseStateMachine
		extends EnumStateMachineConfigurerAdapter<StructureDatabaseState, StructureDatabaseEvent> {

	// 
	private Map<StateMachine<StructureDatabaseState, StructureDatabaseEvent>, StructureDatabase> _map = new HashMap<>();

	public void register(StateMachine<StructureDatabaseState, StructureDatabaseEvent> stateMachine, StructureDatabase structureDatabase) {
		_map.put(stateMachine, structureDatabase);
	}
	
	public void unregister(StateMachine<StructureDatabaseState, StructureDatabaseEvent> stateMachine) {
		_map.remove(stateMachine);
	}
	
	@Override
	public void configure(StateMachineConfigurationConfigurer<StructureDatabaseState, StructureDatabaseEvent> config)
			throws Exception {

		config.withConfiguration().autoStartup(true);
	}

	@Override
	public void configure(StateMachineStateConfigurer<StructureDatabaseState, StructureDatabaseEvent> states)
			throws Exception {

		//
		states.withStates().initial(StructureDatabaseState.INITIAL).choice(StructureDatabaseState.PARSING)
				.states(EnumSet.allOf(StructureDatabaseState.class));

	}

	@Override
	public void configure(StateMachineTransitionConfigurer<StructureDatabaseState, StructureDatabaseEvent> transitions)
			throws Exception {

		transitions
		// @formatter:off
			.withExternal()
				.source(StructureDatabaseState.INITIAL)
				.target(StructureDatabaseState.PARSING)
				.event(StructureDatabaseEvent.PARSE)
				.and()
//			.withExternal()
//				.source(StructureDatabaseState.INITIAL)
//				.target(StructureDatabaseState.NOT_RUNNING)
//				.guard(context -> )
//				.and()				
			//
			.withChoice()
				.source(StructureDatabaseState.PARSING)
				.first(StructureDatabaseState.RUNNING, ctx -> {
					return _map.get(ctx.getStateMachine())._isRunning();
				})
				.last(StructureDatabaseState.NOT_RUNNING);
			// @formatter:on
	}

	
	
	@OnTransition
	public void anyTransition(Transition<StructureDatabaseState, StructureDatabaseEvent> transition) {
		System.out.println("anyTransition: " + transition);
	}
}

package org.slizaa.server.service.slizaa.internal.structuredatabase;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class StructureDatabaseStateMachineConfig
    extends
    EnumStateMachineConfigurerAdapter<StructureDatabaseState, StructureDatabaseEvent> {

  @Override
  public void configure(StateMachineConfigurationConfigurer<StructureDatabaseState, StructureDatabaseEvent> config)
      throws Exception {
    config
        .withConfiguration()
        .autoStartup(true);
  }

  @Override
  public void configure(StateMachineStateConfigurer<StructureDatabaseState, StructureDatabaseEvent> states)
      throws Exception {

    //
    states
        .withStates()
        .initial(StructureDatabaseState.INITIAL)
        .states(EnumSet.allOf(StructureDatabaseState.class));
  }

  @Override
  public void configure(StateMachineTransitionConfigurer<StructureDatabaseState, StructureDatabaseEvent> transitions)
      throws Exception {

    //
    transitions
        //
        .withExternal()
        .source(StructureDatabaseState.INITIAL)
        .target(StructureDatabaseState.PARSING)
        .event(StructureDatabaseEvent.PARSE);
  }

}

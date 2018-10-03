package org.slizaa.server.backend.impl;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;

import java.util.EnumSet;

import static org.slizaa.server.backend.impl.ServerBackendStateMachine.Events.*;
import static org.slizaa.server.backend.impl.ServerBackendStateMachine.States.*;

@Configuration
@EnableStateMachine
public class ServerBackendStateMachine
    extends EnumStateMachineConfigurerAdapter<ServerBackendStateMachine.States, ServerBackendStateMachine.Events> {

  @Autowired
  private BeanFactory beanFactory;

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
        .withExternal()
          .source(INITIAL)
          .target(CHOICE)
          .and()
        .withChoice()
          .source(CHOICE)
          .first(BACKEND_CONFIGURED, guard(), context -> System.out.println(beanFactory))
          .last(BACKEND_UNCONFIGURED)
          .and()
        .withExternal()
          .source(BACKEND_CONFIGURED)
          .target(UPDATING_BACKEND)
          .event(UPDATE_BACKEND)
          .and()
        .withExternal()
          .source(BACKEND_UNCONFIGURED)
          .target(UPDATING_BACKEND)
          .event(UPDATE_BACKEND)
          .and()
        .withExternal()
          .source(UPDATING_BACKEND)
          .target(BACKEND_UNCONFIGURED)
          .event(UPDATE_FAILED)
          .and()
        .withExternal()
          .source(UPDATING_BACKEND)
          .target(BACKEND_CONFIGURED)
          .event(UPDATE_SUCCEDED);
  }

  @Bean
  public Guard<States, Events> guard() {
    return context -> {
      System.out.println("ASNDLASKNDLAKSNDLKASND");
      return true;
    };
  }

  /**
   *
   */
  public static enum States {
    INITIAL, CHOICE, BACKEND_UNCONFIGURED, BACKEND_CONFIGURED, UPDATING_BACKEND
  }

  /**
   *
   */
  public static enum Events {
    CHECK_BACKEND_CONFIGURATION, UPDATE_BACKEND, UPDATE_FAILED, UPDATE_SUCCEDED
  }
}

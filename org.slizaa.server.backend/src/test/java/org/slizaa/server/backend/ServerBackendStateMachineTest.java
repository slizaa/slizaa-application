package org.slizaa.server.backend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slizaa.server.backend.impl.ServerBackendStateMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ServerBackendStateMachineTest.class)
@TestConfiguration
@ComponentScan(basePackageClasses = ServerBackendStateMachineTest.class)
public class ServerBackendStateMachineTest {

  //
  @Autowired
  private StateMachine<ServerBackendStateMachine.States, ServerBackendStateMachine.Events> _stateMachine;

  @Test
  public void testTest() {

    //
    assertThat(_stateMachine).isNotNull();

    // test initial => BACKEND_CONFIGURED
    _stateMachine.start();
    assertThat(_stateMachine.getState().getId()).isEqualTo(ServerBackendStateMachine.States.BACKEND_CONFIGURED);

    //
    testUpdateBackendSucceded();

  }

  /**
   *
   */
  private void testUpdateBackendSucceded() {

    // test BACKEND_CONFIGURED => UPDATING_BACKEND_CONFIGURATION
    _stateMachine.sendEvent(MessageBuilder
        .withPayload(ServerBackendStateMachine.Events.UPDATE_BACKEND_CONFIGURATION)
        .build());

    assertThat(_stateMachine.getState().getId()).isEqualTo(ServerBackendStateMachine.States.UPDATING_BACKEND_CONFIGURATION);

    // test UPDATING_BACKEND_CONFIGURATION => BACKEND_CONFIGURED
    _stateMachine.sendEvent(MessageBuilder
        .withPayload(ServerBackendStateMachine.Events.UPDATE_SUCCEDED)
        .build());

    assertThat(_stateMachine.getState().getId()).isEqualTo(ServerBackendStateMachine.States.BACKEND_CONFIGURED);
  }

  /**
   *
   */
  private void testUpdateBackendFailed() {

    // test BACKEND_CONFIGURED => UPDATING_BACKEND_CONFIGURATION
    _stateMachine.sendEvent(MessageBuilder
        .withPayload(ServerBackendStateMachine.Events.UPDATE_BACKEND_CONFIGURATION)
        .build());

    assertThat(_stateMachine.getState().getId()).isEqualTo(ServerBackendStateMachine.States.UPDATING_BACKEND_CONFIGURATION);

    // test UPDATING_BACKEND_CONFIGURATION => BACKEND_CONFIGURED
    _stateMachine.sendEvent(MessageBuilder
        .withPayload(ServerBackendStateMachine.Events.UPDATE_FAILED)
        .build());

    assertThat(_stateMachine.getState().getId()).isEqualTo(ServerBackendStateMachine.States.BACKEND_UNCONFIGURED);
  }
}


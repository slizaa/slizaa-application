package org.slizaa.server.service.backend;

import org.junit.Test;
import org.slizaa.server.service.backend.impl.ServerBackendStateMachine;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;

import static org.assertj.core.api.Assertions.assertThat;

public class ServerBackendStateMachineTest extends AbstractServerBackendTest {

  @Test
  public void test_UPDATE_BACKEND_CONFIGURATION_Successful() {

    // get the state machine
    StateMachine stateMachine =  applicationContext.getBean(StateMachine.class);
    assertThat(stateMachine).isNotNull();
    assertThat(stateMachine).isSameAs(applicationContext.getBean(StateMachine.class));

    // test initial => BACKEND_CONFIGURED
    stateMachine.start();
    assertThat(stateMachine.getState().getId()).isEqualTo(ServerBackendStateMachine.States.BACKEND_UNCONFIGURED);

    // test BACKEND_CONFIGURED => UPDATING_BACKEND_CONFIGURATION
    stateMachine.sendEvent(MessageBuilder
        .withPayload(ServerBackendStateMachine.Events.UPDATE_BACKEND_CONFIGURATION)
        .build());

    assertThat(stateMachine.getState().getId()).isEqualTo(ServerBackendStateMachine.States.UPDATING_BACKEND_CONFIGURATION);

    // test UPDATING_BACKEND_CONFIGURATION => BACKEND_CONFIGURED
    stateMachine.sendEvent(MessageBuilder
        .withPayload(ServerBackendStateMachine.Events.UPDATE_SUCCEDED)
        .build());

    assertThat(stateMachine.getState().getId()).isEqualTo(ServerBackendStateMachine.States.BACKEND_CONFIGURED);
  }

  @Test
  public void test_UPDATE_BACKEND_CONFIGURATION_Failed() {

    // get the state machine
    StateMachine stateMachine =  applicationContext.getBean(StateMachine.class);
    assertThat(stateMachine).isNotNull();

    // test initial => BACKEND_CONFIGURED
    stateMachine.start();
    assertThat(stateMachine.getState().getId()).isEqualTo(ServerBackendStateMachine.States.BACKEND_UNCONFIGURED);

    // test BACKEND_CONFIGURED => UPDATING_BACKEND_CONFIGURATION
    stateMachine.sendEvent(MessageBuilder
        .withPayload(ServerBackendStateMachine.Events.UPDATE_BACKEND_CONFIGURATION)
        .build());

    assertThat(stateMachine.getState().getId()).isEqualTo(ServerBackendStateMachine.States.UPDATING_BACKEND_CONFIGURATION);

    // test UPDATING_BACKEND_CONFIGURATION => BACKEND_UNCONFIGURED
    stateMachine.sendEvent(MessageBuilder
        .withPayload(ServerBackendStateMachine.Events.UPDATE_FAILED)
        .build());

    assertThat(stateMachine.getState().getId()).isEqualTo(ServerBackendStateMachine.States.BACKEND_UNCONFIGURED);
  }
}

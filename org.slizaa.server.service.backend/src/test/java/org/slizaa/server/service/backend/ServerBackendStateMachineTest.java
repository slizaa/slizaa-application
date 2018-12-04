package org.slizaa.server.service.backend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slizaa.server.service.backend.impl.ServerBackendStateMachine;
import org.slizaa.server.service.extensions.IExtension;
import org.slizaa.server.service.extensions.IExtensionIdentifier;
import org.slizaa.server.service.extensions.IExtensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ServerBackendStateMachineTest.class)
@TestConfiguration
@ComponentScan(basePackageClasses = ServerBackendStateMachineTest.class)
public class ServerBackendStateMachineTest {

  @Bean
  public IExtensionService extensionService() {
    return new IExtensionService() {
      @Override
      public List<IExtension> getExtensions() {
        return Collections.emptyList();
      }

      @Override
      public List<IExtension> getExtensions(List<IExtensionIdentifier> extensionIdentifiers) {
        return Collections.emptyList();
      }
    };
  }

  //
  @Autowired
  private ApplicationContext _applicationContext;

  @Test
  public void testTest() {

    StateMachine stateMachine =  _applicationContext.getBean(StateMachine.class);

    //
    assertThat(stateMachine).isNotNull();

    // test initial => BACKEND_CONFIGURED
    stateMachine.start();
    assertThat(stateMachine.getState().getId()).isEqualTo(ServerBackendStateMachine.States.BACKEND_UNCONFIGURED);

    // TODO:
    // testUpdateBackendSucceded(stateMachine);
  }

  /**
   *
   */
  private void testUpdateBackendSucceded(StateMachine stateMachine) {

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
//
//  /**
//   *
//   */
//  private void testUpdateBackendFailed() {
//
//    // test BACKEND_CONFIGURED => UPDATING_BACKEND_CONFIGURATION
//    stateMachine.sendEvent(MessageBuilder
//        .withPayload(ServerBackendStateMachine.Events.UPDATE_BACKEND_CONFIGURATION)
//        .build());
//
//    assertThat(stateMachine.getState().getId()).isEqualTo(ServerBackendStateMachine.States.UPDATING_BACKEND_CONFIGURATION);
//
//    // test UPDATING_BACKEND_CONFIGURATION => BACKEND_CONFIGURED
//    stateMachine.sendEvent(MessageBuilder
//        .withPayload(ServerBackendStateMachine.Events.UPDATE_FAILED)
//        .build());
//
//    assertThat(stateMachine.getState().getId()).isEqualTo(ServerBackendStateMachine.States.BACKEND_UNCONFIGURED);
//  }
}


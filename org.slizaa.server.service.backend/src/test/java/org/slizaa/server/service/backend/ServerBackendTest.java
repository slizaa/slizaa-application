package org.slizaa.server.service.backend;

import org.junit.Test;
import org.slizaa.server.service.backend.impl.ServerBackendStateMachine;
import org.slizaa.server.service.backend.impl.SlizaaServerBackendImpl;
import org.slizaa.server.service.extensions.IExtension;
import org.slizaa.server.service.extensions.Version;
import org.slizaa.server.service.extensions.mvn.MvnBasedExtension;
import org.slizaa.server.service.extensions.mvn.MvnDependency;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ServerBackendTest extends AbstractServerBackendTest {

  //
  private static MvnBasedExtension neo4jExtension = new MvnBasedExtension("Neo4j Slizaa Backend", new Version(1, 0, 0))
      .withDependency(new MvnDependency("org.slizaa.neo4j:org.slizaa.neo4j.importer:1.0.0-SNAPSHOT", "*:org.slizaa.scanner.spi-api", "*:jdk.tools"))
      .withDependency(new MvnDependency("org.slizaa.neo4j:org.slizaa.neo4j.graphdbfactory:1.0.0-SNAPSHOT", "*:org.slizaa.scanner.spi-api"));

  /**
   *
   */
  public ServerBackendTest() {
    super(Collections.singletonList(neo4jExtension));
  }

  /**
   *
   */
  @Test
  public void test_ServerBackend() {

    SlizaaServerBackendImpl backend = applicationContext.getBean(SlizaaServerBackendImpl.class);
    assertThat(backend).isNotNull();

    assertThat(backend.isConfigured());
  }
}


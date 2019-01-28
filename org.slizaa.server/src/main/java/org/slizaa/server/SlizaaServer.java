package org.slizaa.server;

import org.slizaa.server.graphql.EnableGraphqlModule;
import org.slizaa.server.service.backend.EnableBackendServiceModule;
import org.slizaa.server.service.configuration.EnableConfigurationModule;
import org.slizaa.server.service.extensions.EnableExtensionsModule;
import org.slizaa.server.service.slizaa.EnableSlizaaServiceModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
@SpringBootApplication
@EnableExtensionsModule
@EnableBackendServiceModule
@EnableSlizaaServiceModule
@EnableGraphqlModule
@EnableConfigurationModule
public class SlizaaServer {

  /**
   *
   * @param args
   */
  public static void main(String[] args) {
    SpringApplication.run(SlizaaServer.class, args);
  }
}
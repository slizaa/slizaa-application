package org.slizaa.server.service.slizaa.internal.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
public class GraphDatabaseHierarchicalGraphConfiguration {

  @JsonProperty("identifier")
  private String _identifier;

  public GraphDatabaseHierarchicalGraphConfiguration(String identifier) {
    _identifier = identifier;
  }

  public GraphDatabaseHierarchicalGraphConfiguration() {
  }

  public String getIdentifier() {
    return _identifier;
  }
}

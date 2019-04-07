package org.slizaa.server.service.slizaa.internal.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
public class SlizaaServiceConfiguration {

  @JsonProperty("graphDatabases")
  private List<GraphDatabaseConfiguration> _graphDatabases = new ArrayList<>();

  /**
   * Returns the graph databases configurations.
   * 
   * @return
   */
  public List<GraphDatabaseConfiguration> getGraphDatabases() {
    return _graphDatabases;
  }
}

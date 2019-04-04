package org.slizaa.server.service.slizaa.internal.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class SlizaaServiceConfiguration {

  @JsonProperty("graphDatabases")
  private List<GraphDatabaseConfiguration> _graphDatabases = new ArrayList<>();

  public List<GraphDatabaseConfiguration> getGraphDatabases() {
    return _graphDatabases;
  }
}

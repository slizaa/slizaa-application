package org.slizaa.server.service.slizaa.internal.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slizaa.server.service.slizaa.IGraphDatabase;
import org.slizaa.server.service.slizaa.IHierarchicalGraph;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
public class GraphDatabaseConfiguration {

  @JsonProperty("identifier")
  private String                     _identifier;

  @JsonProperty("isRunning")
  private boolean                    _isRunning;

  @JsonProperty("port")
  private int                        _port;

  @JsonProperty("hierarchicalGraphs")
  private List<GraphDatabaseHierarchicalGraphConfiguration> _hierarchicalGraphs = new ArrayList<>();

  @JsonProperty("contentDefinition")
  private GraphDatabaseContentDefinitionConfiguration _contentDefinitionCfg;

  protected GraphDatabaseConfiguration() {
  }

  public GraphDatabaseConfiguration(IGraphDatabase graphDatabase) {
    _identifier = graphDatabase.getIdentifier();
    _isRunning = graphDatabase.isRunning();
    _port = graphDatabase.getPort();

    for (IHierarchicalGraph hierarchicalGraph : graphDatabase.getHierarchicalGraphs()) {
      _hierarchicalGraphs.add(new GraphDatabaseHierarchicalGraphConfiguration(hierarchicalGraph.getIdentifier()));
    }

    //
    if (graphDatabase.getContentDefinitionProvider() != null) {

      String contentDefinitionFactoryId = graphDatabase.getContentDefinitionProvider()
          .getContentDefinitionProviderFactory().getFactoryId();

      String externalRepresenation = graphDatabase.getContentDefinitionProvider().toExternalRepresentation();

      _contentDefinitionCfg = new GraphDatabaseContentDefinitionConfiguration(contentDefinitionFactoryId, externalRepresenation);
    }
  }

  public String getIdentifier() {
    return _identifier;
  }

  public boolean isRunning() {
    return _isRunning;
  }

  public int getPort() {
    return _port;
  }

  public List<GraphDatabaseHierarchicalGraphConfiguration> getHierarchicalGraphs() {
    return _hierarchicalGraphs;
  }

  public GraphDatabaseContentDefinitionConfiguration getContentDefinitionCfg() {
    return _contentDefinitionCfg;
  }
}

package org.slizaa.server.service.slizaa.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import org.slizaa.server.service.slizaa.IGraphDatabase;
import org.slizaa.server.service.slizaa.IHierarchicalGraph;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SlizaaServiceConfiguration {

  @JsonProperty("graphDatabases")
  private List<GraphDbCfg> _graphDatabases = new ArrayList<>();

  public List<GraphDbCfg> getGraphDatabases() {
    return _graphDatabases;
  }

  /**
   * 
   * @author Gerd W&uuml;therich (gw@code-kontor.io)
   */
  public static class GraphDbCfg {

    private String                     _identifier;

    private boolean                    _isRunning;

    private int                        _port;

    @JsonProperty("hierarchicalGraphs")
    private List<HierarchicalGraphCfg> _hierarchicalGraphs = new ArrayList<>();

    @JsonProperty("contentDefinition")
    private ContentDefinitionCfg       _contentDefinitionCfg;

    protected GraphDbCfg() {
    }

    public GraphDbCfg(IGraphDatabase graphDatabase) {
      _identifier = graphDatabase.getIdentifier();
      _isRunning = graphDatabase.isRunning();
      _port = graphDatabase.getPort();

      for (IHierarchicalGraph hierarchicalGraph : graphDatabase.getHierarchicalGraphs()) {
        _hierarchicalGraphs.add(new HierarchicalGraphCfg(hierarchicalGraph.getIdentifier()));
      }

      //
      if (graphDatabase.getContentDefinitionProvider() != null) {
        
        String contentDefinitionFactoryId = graphDatabase.getContentDefinitionProvider()
            .getContentDefinitionProviderFactory().getFactoryId();
        
        String externalRepresenation = graphDatabase.getContentDefinitionProvider().toExternalRepresentation();
        
        _contentDefinitionCfg = new ContentDefinitionCfg(contentDefinitionFactoryId, externalRepresenation);
      }
    }

    public String getIdentifier() {
      return _identifier;
    }

    public void setIdentifier(String identifier) {
      _identifier = identifier;
    }

    public boolean isRunning() {
      return _isRunning;
    }

    public void setRunning(boolean isRunning) {
      _isRunning = isRunning;
    }

    public int getPort() {
      return _port;
    }

    public void setPort(int port) {
      _port = port;
    }

    public List<HierarchicalGraphCfg> getHierarchicalGraphs() {
      return _hierarchicalGraphs;
    }

    public ContentDefinitionCfg getContentDefinitionCfg() {
      return _contentDefinitionCfg;
    }
  }

  /**
   * 
   * @author Gerd W&uuml;therich (gw@code-kontor.io)
   */
  public static class HierarchicalGraphCfg {

    private String _identifier;

    public HierarchicalGraphCfg(String identifier) {
      _identifier = identifier;
    }

    public HierarchicalGraphCfg() {
    }

    public String getIdentifier() {
      return _identifier;
    }

    public void setIdentifier(String identifier) {
      _identifier = identifier;
    }
  }

  /**
   * 
   * @author Gerd W&uuml;therich (gw@code-kontor.io)
   */
  public static class ContentDefinitionCfg {

    private String _contentDefinitionFactoryId;

    private String _externalRepresentation;

    public ContentDefinitionCfg() {
    }

    public ContentDefinitionCfg(String contentDefinitionFactoryId, String externalRepresenation) {
      _contentDefinitionFactoryId = checkNotNull(contentDefinitionFactoryId);
      _externalRepresentation = checkNotNull(externalRepresenation);
    }

    public String getContentDefinitionFactoryId() {
      return _contentDefinitionFactoryId;
    }

    public void setContentDefinitionFactoryId(String contentDefinitionFactoryId) {
      _contentDefinitionFactoryId = contentDefinitionFactoryId;
    }

    public String getExternalRepresentation() {
      return _externalRepresentation;
    }

    public void setExternalRepresentation(String externalRepresentation) {
      _externalRepresentation = externalRepresentation;
    }
  }
}

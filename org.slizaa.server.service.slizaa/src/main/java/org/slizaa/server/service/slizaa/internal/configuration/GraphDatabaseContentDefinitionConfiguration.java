package org.slizaa.server.service.slizaa.internal.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
public class GraphDatabaseContentDefinitionConfiguration {

  @JsonProperty("factoryId")
  private String _factoryId;

  @JsonProperty("contentDefinition")
  private String _contentDefinition;

  public GraphDatabaseContentDefinitionConfiguration() {
  }

  public GraphDatabaseContentDefinitionConfiguration(String factoryId, String contentDefinition) {
    _factoryId = checkNotNull(factoryId);
    _contentDefinition = checkNotNull(contentDefinition);
  }

  public String getFactoryId() {
    return _factoryId;
  }

  public String getContentDefinition() {
    return _contentDefinition;
  }
}

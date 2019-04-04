package org.slizaa.server.service.slizaa.internal.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
public class GraphDatabaseContentDefinitionConfiguration {

    @JsonProperty("factoryId")
    private String _contentDefinitionFactoryId;

    @JsonProperty("contentDefinition")
    private String _externalRepresentation;

    public GraphDatabaseContentDefinitionConfiguration() {
    }

    public GraphDatabaseContentDefinitionConfiguration(String contentDefinitionFactoryId, String externalRepresenation) {
        _contentDefinitionFactoryId = checkNotNull(contentDefinitionFactoryId);
        _externalRepresentation = checkNotNull(externalRepresenation);
    }

    public String getContentDefinitionFactoryId() {
        return _contentDefinitionFactoryId;
    }

    public String getExternalRepresentation() {
        return _externalRepresentation;
    }
}

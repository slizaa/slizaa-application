package org.slizaa.server.graphql.graphdatabase;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import org.slizaa.server.service.slizaa.IGraphDatabase;

public class GraphDatabase {

  private String                            _identifier;

  private String                            _state;

  private String[]                          _availableActions;

  private Collection<ContentDefinitionType> _availableContentDefinitionTypes;

  private int                               _port;

  private ContentDefinition                 _currentContentDefinition;

  private GraphDatabase(String identifier, String state, int port, String[] availableActions,
      List<ContentDefinitionType> availableContentDefinitionTypes, ContentDefinition contentDefinition) {
    this._identifier = identifier;
    this._state = state;
    this._port = port;
    this._currentContentDefinition = contentDefinition;
    this._availableActions = availableActions;
    this._availableContentDefinitionTypes = availableContentDefinitionTypes;
  }

  public String getIdentifier() {
    return _identifier;
  }

  public String getState() {
    return _state;
  }

  public int getPort() {
    return _port;
  }

  public ContentDefinition getCurrentContentDefinition() {
    return _currentContentDefinition;
  }

  public Collection<ContentDefinitionType> getAvailableContentDefinitionTypes() {
    return _availableContentDefinitionTypes;
  }

  public String[] availableActions() {
    return _availableActions;
  }

  /**
   * @param database
   * @return
   */
  public static GraphDatabase convert(IGraphDatabase database) {

    // get the content definition provider
    IContentDefinitionProvider<?> contentDefinitionProvider = database.getContentDefinition();

    ContentDefinition contentDefinition = null;

    // the content definition
    if (contentDefinitionProvider != null) {

      contentDefinition = new ContentDefinition(
          new ContentDefinitionType(contentDefinitionProvider.getContentDefinitionProviderFactory().getFactoryId(),
              contentDefinitionProvider.getContentDefinitionProviderFactory().getName(),
              contentDefinitionProvider.getContentDefinitionProviderFactory().getDescription()),
          contentDefinitionProvider.toExternalRepresentation());
    }

    // return the database
    return new GraphDatabase(database.getIdentifier(), database.getState().name(), database.getPort(),
        database.getAvailableActions().stream().map(action -> action.getName()).toArray(String[]::new),
        database.contentDefinitionFactories().stream()
            .map(fact -> new ContentDefinitionType(fact.getFactoryId(), fact.getName(), fact.getDescription()))
            .collect(Collectors.toList()),
        contentDefinition);
  }
}

package org.slizaa.server.graphql.graphdatabase;

public class ContentDefinition {
  
  private ContentDefinitionType _type;

  private String _definition;

  public ContentDefinition(ContentDefinitionType currentContentDefinitionType, String definition) {
    _type = currentContentDefinitionType;
    _definition = definition;
  }

  public ContentDefinitionType getType() {
    return _type;
  }

  public String getDefinition() {
    return _definition;
  }
}

package org.slizaa.server.graphql.graphdatabase;

public class ContentDefinition {
  
  private ContentDefinitionType _contentDefinitionType;

  private String _definition;

  public ContentDefinition(ContentDefinitionType contentDefinitionType, String definition) {
    this._contentDefinitionType = _contentDefinitionType;
    this._definition = definition;
  }

  public ContentDefinitionType getContentDefinitionType() {
    return _contentDefinitionType;
  }

  public String getDefinition() {
    return _definition;
  }
}

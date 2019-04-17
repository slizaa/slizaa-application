package org.slizaa.server.graphql.graphdatabase;

public class ContentDefinitionType {

  private String _identifier;

  private String _name;

  private String _description;

  public ContentDefinitionType(String identifier, String name, String description) {
    _identifier = identifier;
    _name = name;
    _description = description;
  }

  public String getIdentifier() {
    return _identifier;
  }

  public String getName() {
    return _name;
  }

  public String getDescription() {
    return _description;
  }
}

package org.slizaa.server.graphql.graphdatabase;

public class ContentDefinitionType {

  private String _factoryId;

  private String _name;

  private String _description;

  public ContentDefinitionType(String factoryId, String name, String description) {
    _factoryId = factoryId;
    _name = name;
    _description = description;
  }

  public String getFactoryId() {
    return _factoryId;
  }

  public String getName() {
    return _name;
  }

  public String getDescription() {
    return _description;
  }
}

package org.slizaa.server.graphql.graphdatabase;

public class ContentDefinition {
  
  private String _factoryId;
  
  private String _name;
  
  private String _description;
  
  private String _definition;
  
  public ContentDefinition(String factoryId, String name, String description, String definition) {
    super();
    _factoryId = factoryId;
    _name = name;
    _description = description;
    _definition = definition;
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

  public String getDefinition() {
    return _definition;
  }


}

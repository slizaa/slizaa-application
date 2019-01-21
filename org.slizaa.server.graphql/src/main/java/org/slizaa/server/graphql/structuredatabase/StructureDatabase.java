package org.slizaa.server.graphql.structuredatabase;

public class StructureDatabase {

  private String identifier;
  
  public StructureDatabase(String identifier) {
    this.identifier = identifier;
  }

  public String getIdentifier() {
    return identifier;
  }
}

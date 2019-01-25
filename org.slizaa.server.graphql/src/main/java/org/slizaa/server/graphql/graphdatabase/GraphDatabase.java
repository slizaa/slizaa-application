package org.slizaa.server.graphql.graphdatabase;

import java.util.Collections;
import java.util.List;

public class GraphDatabase {

  private String identifier;
  
  public GraphDatabase(String identifier) {
    this.identifier = identifier;
  }

  public String getIdentifier() {
    return identifier;
  }
}

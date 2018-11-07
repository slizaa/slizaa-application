package org.slizaa.server.graphql;

public class Node {
  private String id;
  private String name;

  public Node(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }
}

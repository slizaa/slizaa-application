package org.slizaa.server.graphql;

/**
 *
 */
public class Dependency {

  private String id;

  private String sourceNodeId;

  private String targetNodeId;

  public Dependency(String id, String sourceNodeId, String targetNodeId) {
    this.id = id;
    this.sourceNodeId = sourceNodeId;
    this.targetNodeId = targetNodeId;
  }
}

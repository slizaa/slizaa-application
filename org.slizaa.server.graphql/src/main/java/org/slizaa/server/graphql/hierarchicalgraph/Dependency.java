package org.slizaa.server.graphql.hierarchicalgraph;

/**
 *
 */
public class Dependency {

  private String id;

  private Node sourceNode;

  private Node targetNode;

  private int weight;

  public Dependency(String id, Node sourceNode, Node targetNode, int weight) {
    this.id = id;
    this.sourceNode = sourceNode;
    this.targetNode = targetNode;
    this.weight = weight;
  }

  public Dependency(Node sourceNode, Node targetNode, int weight) {
    this.sourceNode = sourceNode;
    this.targetNode = targetNode;
    this.weight = weight;
  }

  public int getWeight() {
    return weight;
  }
}

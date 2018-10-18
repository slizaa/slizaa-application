package org.slizaa.server.rest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Node {

  /** - */
  @JsonProperty("id")
  private final long    _id;

  /** - */
  @JsonProperty("label")
  private final String  _label;

  /** - */
  @JsonProperty("children")
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private final Node[]  _children;

  /** - */
  @JsonProperty("hasUnresolvedChildren")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private final Boolean _hasUnresolvedChildren;

  /**
   * <p>
   * Creates a new instance of type {@link Node}.
   * </p>
   *
   * @param id
   * @param children
   * @param hasUnresolvedChildren
   */
  public Node(long id, boolean hasUnresolvedChildren, String label, Node... children) {
    this._id = id;
    this._hasUnresolvedChildren = hasUnresolvedChildren ? true : null;
    this._children = children;
    this._label = label;
  }

  /**
   * <p>
   * Creates a new instance of type {@link Node}.
   * </p>
   *
   * @param id
   * @param hasUnresolvedChildren
   * @param children
   */
  public Node(long id, boolean hasUnresolvedChildren, String label, List<Node> children) {
    this._id = id;
    this._hasUnresolvedChildren = hasUnresolvedChildren ? true : null;
    this._children = children.toArray(new Node[0]);
    this._label = label;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public long getId() {
    return this._id;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public Node[] getChildren() {
    return this._children;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public Boolean hasUnresolvedChildren() {
    return this._hasUnresolvedChildren;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public String getLabel() {
    return this._label;
  }
}
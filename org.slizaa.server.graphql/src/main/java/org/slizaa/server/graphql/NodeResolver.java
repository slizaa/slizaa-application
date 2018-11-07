package org.slizaa.server.graphql;

import com.coxautodev.graphql.tools.GraphQLResolver;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 *
 */
public class NodeResolver implements GraphQLResolver<Node> {

  /**
   *
   * @param node
   * @return
   */
  public Optional<Node> getParent(Node node) {
    return Optional.of(node);
  }

  public Optional<List<Node>> getChildren(Node node) {
    return Optional.of(Collections.singletonList(node));
  }
}
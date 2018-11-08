package org.slizaa.server.graphql;

import com.coxautodev.graphql.tools.GraphQLResolver;
import org.slizaa.hierarchicalgraph.graphdb.model.GraphDbNodeSource;
import org.slizaa.server.service.slizaa.ISlizaaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 */
@Component
public class NodeResolver implements GraphQLResolver<Node> {

  //
  @Autowired
  private ISlizaaService slizaaService;

  /**
   * @param node
   * @return
   */
  public Optional<Node> getParent(Node node) {
    return node.getHgNode().getParent() != null ? Optional.of(new Node(node.getHgNode().getParent())) : Optional.empty();
  }

  /**
   *
   * @param node
   * @return
   */
  public List<Node> getChildren(Node node) {
    return node.getHgNode().getChildren().stream().map(hgNode -> new Node(hgNode)).collect(Collectors.toList());
  }

  /**
   *
   * @param node
   * @return
   */
  public List<Node> getPredecessors(Node node) {
    return node.getHgNode().getPredecessors().stream().map(hgNode -> new Node(hgNode)).collect(Collectors.toList());
  }


  /**
   * @param node
   * @return
   */
  public List<MapEntry> getProperties(Node node) {

    // TODO: GraphQL - Extension?
    GraphDbNodeSource nodeSource = node.getHgNode().getNodeSource(GraphDbNodeSource.class).get();
    return nodeSource.getProperties().stream().map(entry -> new MapEntry(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
  }

  public List<Dependency> getDependenciesTo(Node node, List<String> targetNodeIds) {
    return Collections.singletonList(new Dependency("SHCNAP", "source", "target"));
  }
}
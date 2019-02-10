package org.slizaa.server.graphql.hierarchicalgraph;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;
import org.slizaa.hierarchicalgraph.graphdb.model.GraphDbNodeSource;
import org.slizaa.server.service.slizaa.ISlizaaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLResolver;

/**
 *
 */
@Component
public class NodeResolver implements GraphQLResolver<Node> {

  //
  @Autowired
  private ISlizaaService slizaaService;

  public String getIconIdentifier(Node node) {

    ILabelDefinitionProvider.ILabelDefinition labelDefinition = node.labelDefinitionProvider()
        .getLabelDefinition(node.getHgNode());

    if (labelDefinition.isOverlayImage()) {
      return slizaaService.getSvgService().createSvgAndReturnShortKey(labelDefinition.getBaseImagePath(),
          labelDefinition.getOverlayImagePath(ILabelDefinitionProvider.OverlayPosition.TOP_LEFT),
          labelDefinition.getOverlayImagePath(ILabelDefinitionProvider.OverlayPosition.TOP_RIGHT),
          labelDefinition.getOverlayImagePath(ILabelDefinitionProvider.OverlayPosition.BOTTOM_LEFT),
          labelDefinition.getOverlayImagePath(ILabelDefinitionProvider.OverlayPosition.BOTTOM_RIGHT));
    } else {
      return slizaaService.getSvgService().createSvgAndReturnShortKey(labelDefinition.getBaseImagePath());
    }
  }

  /**
   * @param node
   * @return
   */
  public Optional<Node> getParent(Node node) {
    return node.getHgNode().getParent() != null ? Optional.of(new Node(node.getHgNode().getParent()))
        : Optional.empty();
  }

  /**
   *
   * @param node
   * @return
   */
  public List<Node> getChildren(Node node) {
    return node.getHgNode().getChildren().stream().map(hgNode -> new Node(hgNode)).collect(Collectors.toList());
  }

  public boolean hasChildren(Node node) {
    return !node.getHgNode().getChildren().isEmpty();
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

    // TODO!
    return Collections.singletonList(new Dependency(null, null, 1));
  }
}
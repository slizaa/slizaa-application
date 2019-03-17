package org.slizaa.server.graphql.hierarchicalgraph;

import java.util.List;
import java.util.stream.Collectors;

import org.slizaa.hierarchicalgraph.core.model.HGNode;

public class StronglyConnectedComponent extends AbstractNodeSet {

  private List<Integer> _nodePositions;

  public StronglyConnectedComponent(List<HGNode> nodes, List<HGNode> orderedNodes) {
    super(nodes);

    _nodePositions = nodes.stream().map(node -> orderedNodes.indexOf(node)).sorted().collect(Collectors.toList());
  }
  
  public List<Integer> getNodePositions() {
    return _nodePositions;
  }
}

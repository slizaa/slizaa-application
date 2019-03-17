package org.slizaa.server.graphql.hierarchicalgraph;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slizaa.hierarchicalgraph.core.algorithms.GraphUtils;
import org.slizaa.hierarchicalgraph.core.algorithms.IDependencyStructureMatrix;
import org.slizaa.hierarchicalgraph.core.model.HGNode;

public class NodeSet extends AbstractNodeSet {

  public NodeSet(List<HGNode> hgNodeSet) {
    super(hgNodeSet);
  }

  public NodeSet referencedNodes(boolean includePredecessors) {

    Stream<HGNode> nodeStream = hgNodeSet().stream()
        .flatMap(hgNode -> hgNode.getAccumulatedOutgoingCoreDependencies().stream()).map(dep -> dep.getTo());

    if (includePredecessors) {
      nodeStream = nodeStream.flatMap(node -> Stream.concat(node.getPredecessors().stream(), Stream.of(node)));
    }

    List<HGNode> nodes = nodeStream.distinct().collect(Collectors.toList());

    return new NodeSet(nodes);
  }

  /**
   * @param ids
   * @return
   */
  public DependencyMatrix dependencyMatrix() {
    
    return new DependencyMatrix(GraphUtils.createDependencyStructureMatrix(hgNodeSet()));
  }
}

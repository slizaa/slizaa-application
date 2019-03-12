package org.slizaa.server.graphql.hierarchicalgraph;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slizaa.hierarchicalgraph.core.algorithms.GraphUtils;
import org.slizaa.hierarchicalgraph.core.algorithms.IDependencyStructureMatrix;
import org.slizaa.hierarchicalgraph.core.model.HGNode;

public class NodeSet {

  private List<HGNode> _hgNodeSet;

  public NodeSet(List<HGNode> hgNodeSet) {
    this._hgNodeSet = checkNotNull(hgNodeSet);
  }

  public List<Node> getNodes() {
    return _hgNodeSet.stream().map(hgNode -> new Node(hgNode)).collect(Collectors.toList());
  }

  public List<String> getNodeIds() {
    return _hgNodeSet.stream().map(hgNode -> hgNode.getIdentifier().toString()).collect(Collectors.toList());
  }

  public NodeSet referencedNodes(boolean includePredecessors) {

    //
    Stream<HGNode> nodeStream = _hgNodeSet.stream()
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

    //
    IDependencyStructureMatrix dependencyStructureMatrix = GraphUtils.createDependencyStructureMatrix(_hgNodeSet);

    //
    List<Node> orderedNodes = dependencyStructureMatrix.getOrderedNodes().stream().map(hgNode -> new Node(hgNode))
        .collect(Collectors.toList());

    //
    return new DependencyMatrix(orderedNodes, dependencyStructureMatrix.getMatrix());
  }
}

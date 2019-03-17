package org.slizaa.server.graphql.hierarchicalgraph;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.stream.Collectors;

import org.slizaa.hierarchicalgraph.core.model.HGNode;

public abstract class AbstractNodeSet {

  private List<HGNode> _hgNodeSet;

  public AbstractNodeSet(List<HGNode> hgNodeSet) {
    this._hgNodeSet = checkNotNull(hgNodeSet);
  }

  public List<Node> getNodes() {
    return _hgNodeSet.stream().map(hgNode -> new Node(hgNode)).collect(Collectors.toList());
  }

  public List<String> getNodeIds() {
    return _hgNodeSet.stream().map(hgNode -> hgNode.getIdentifier().toString()).collect(Collectors.toList());
  }
  
  protected List<HGNode> hgNodeSet() {
    return _hgNodeSet;
  }
}

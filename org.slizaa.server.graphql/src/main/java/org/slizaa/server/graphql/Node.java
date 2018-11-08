package org.slizaa.server.graphql;

import org.slizaa.hierarchicalgraph.core.model.HGNode;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public class Node {

  // the node identifier
  private String id;

  // the text of the node
  private String text;

  //
  private transient HGNode hgNode;

  //
  private transient ILabelDefinitionProvider labelDefinitionProvider;

  /**
   */
  public Node(HGNode hgNode) {
    this.hgNode = checkNotNull(hgNode);

    //
    labelDefinitionProvider = Utils.getLabelDefinitionProvider(hgNode);
  }

  /**
   *
   * @return
   */
  public String getId() {
    return hgNode.getIdentifier().toString();
  }

  /**
   *
   * @return
   */
  public String getText() {
    // TODO cache
    return labelDefinitionProvider.getLabelDefinition(hgNode).getText();
  }

  /**
   *
   * @return
   */
  HGNode getHgNode() {
    return hgNode;
  }
}
package org.slizaa.server.graphql.hierarchicalgraph;

import org.slizaa.hierarchicalgraph.core.model.HGNode;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;

import static com.google.common.base.Preconditions.checkNotNull;

public class Utils {

  /**
   * @param node
   * @return
   */
  public static ILabelDefinitionProvider getLabelDefinitionProvider(HGNode node) {

    //
    return checkNotNull(checkNotNull(node).getRootNode()
        .getExtension(ILabelDefinitionProvider.class));
  }
}

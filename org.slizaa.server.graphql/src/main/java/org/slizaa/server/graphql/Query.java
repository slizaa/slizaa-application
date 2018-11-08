package org.slizaa.server.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.slizaa.hierarchicalgraph.core.model.HGNode;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;
import org.slizaa.server.service.slizaa.ISlizaaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
@Component
public class Query implements GraphQLQueryResolver {

  //
  @Autowired
  private ISlizaaService slizaaService;

  /**
   *
   * @return
   */
  public Node rootNode() {
    return new Node(slizaaService.getRootNode());
  }

  /**
   *
   * @param id
   * @return
   */
  public Node node(String id) {
    return new Node(slizaaService.getRootNode().lookupNode(Long.parseLong(id)));
  }

  public List<Node> nodes(List<String> ids) {
    // TODO
    return Collections.emptyList();
  }



}
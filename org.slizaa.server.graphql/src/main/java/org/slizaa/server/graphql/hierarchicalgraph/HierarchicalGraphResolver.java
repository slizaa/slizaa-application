package org.slizaa.server.graphql.hierarchicalgraph;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slizaa.hierarchicalgraph.core.model.HGNode;
import org.slizaa.hierarchicalgraph.core.model.HGRootNode;
import org.slizaa.server.service.slizaa.IGraphDatabase;
import org.slizaa.server.service.slizaa.IHierarchicalGraph;
import org.slizaa.server.service.slizaa.ISlizaaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLResolver;

@Component
public class HierarchicalGraphResolver implements GraphQLResolver<HierarchicalGraph> {

  //
  @Autowired
  private ISlizaaService slizaaService;

  /**
   * @return
   */
  public Node rootNode(HierarchicalGraph hierarchicalGraph) {
    return nullSafe(hierarchicalGraph, hgRootNode -> new Node(hgRootNode));
  }

  /**
   * @param id
   * @return
   */
  public Node node(HierarchicalGraph hierarchicalGraph, String id) {
    return nullSafe(hierarchicalGraph, hgRootNode -> {
      HGNode hgNode = "-1".equals(id) ? hgRootNode : hgRootNode.lookupNode(Long.parseLong(id));
      return hgNode != null ? new Node(hgNode) : null;
    });
  }

  /**
   * @param ids
   * @return
   */
  public NodeSet nodes(HierarchicalGraph hierarchicalGraph, List<String> ids) {

    List<HGNode> nodes = ids.stream().map(id -> hgNode(hierarchicalGraph, id)).filter(node -> node != null)
        .collect(Collectors.toList());

    return new NodeSet(nodes);
  }

  private HGNode hgNode(HierarchicalGraph hierarchicalGraph, String id) {
    return nullSafe(hierarchicalGraph, hgRootNode -> {
      return "-1".equals(id) ? hgRootNode : hgRootNode.lookupNode(Long.parseLong(id));
    });
  }

  /**
   * @param function
   * @param <T>
   * @return
   */
  private <T> T nullSafe(HierarchicalGraph hierarchicalGraph, Function<HGRootNode, T> function) {

    // lookup the root node
    IGraphDatabase graphDatabase = slizaaService.getGraphDatabase(hierarchicalGraph.getDatabaseIdentifier());
    if (graphDatabase != null) {
      IHierarchicalGraph hg = graphDatabase.getHierarchicalGraph(hierarchicalGraph.getIdentifier());
      if (hg != null) {
        HGRootNode rootNode = hg.getRootNode();
        if (rootNode != null) {
          return function.apply(rootNode);
        }
      }
    }

    return null;
  }
}

package org.slizaa.server.graphql.hierarchicalgraph;

import com.coxautodev.graphql.tools.GraphQLResolver;
import org.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;
import org.slizaa.hierarchicalgraph.core.model.HGNode;
import org.slizaa.hierarchicalgraph.core.model.HGRootNode;
import org.slizaa.server.service.slizaa.IGraphDatabase;
import org.slizaa.server.service.slizaa.IHierarchicalGraph;
import org.slizaa.server.service.slizaa.ISlizaaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
            HGNode hgNode = hgRootNode.lookupNode(Long.parseLong(id));
            return hgNode != null ? new Node(hgNode) : null;
        });
    }

    /**
     * @param ids
     * @return
     */
    public List<Node> nodes(HierarchicalGraph hierarchicalGraph, List<String> ids) {
        return ids.stream().map(id -> node(hierarchicalGraph, id)).filter(node -> node != null).collect(Collectors.toList());
    }

    /**
     * @param ids
     * @return
     */
    public DependencyMatrix dependencyMatrix(HierarchicalGraph hierarchicalGraph, List<String> ids) {

        //
        List<Node> nodes = nodes(hierarchicalGraph, ids);

        //
        List<List<Dependency>> dependencies = nodes.stream().map(nodeFrom -> {
            return nodes.stream().map(nodeTo -> {
                HGAggregatedDependency aggregatedDependency = nodeFrom.getHgNode().getOutgoingDependenciesTo(nodeTo.getHgNode());
                return new Dependency(nodeFrom, nodeTo, aggregatedDependency != null ? aggregatedDependency.getAggregatedWeight() : 0);
            }).collect(Collectors.toList());
        }).collect(Collectors.toList());

        //
        return new DependencyMatrix(nodes, dependencies);
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
            IHierarchicalGraph hg = graphDatabase.getHierarchicalGraph(hierarchicalGraph.getHierarchicalGraphIdentifier());
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

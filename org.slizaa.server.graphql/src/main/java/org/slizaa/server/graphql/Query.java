package org.slizaa.server.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;
import org.slizaa.hierarchicalgraph.core.model.HGNode;
import org.slizaa.hierarchicalgraph.core.model.HGRootNode;
import org.slizaa.server.service.slizaa.ISlizaaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        return nullSafe(hgRootNode -> new Node(hgRootNode));
    }

    /**
     *
     * @param id
     * @return
     */
    public Node node(String id) {
        return nullSafe(hgRootNode -> {
            HGNode hgNode = hgRootNode.lookupNode(Long.parseLong(id));
            return hgNode != null ? new Node(slizaaService.getRootNode().lookupNode(Long.parseLong(id))) : null;
        });
    }

    /**
     *
     * @param ids
     * @return
     */
    public List<Node> nodes(List<String> ids) {
        return ids.stream().map(id -> node(id)).filter(node -> node != null).collect(Collectors.toList());
    }

    /**
     *
     * @param ids
     * @return
     */
    public DependencyMatrix dependencyMatrix(List<String> ids) {

        //
        List<Node> nodes = nodes(ids);

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
     *
     * @param function
     * @param <T>
     * @return
     */
    private <T> T nullSafe(Function<HGRootNode, T> function) {

        // lookup the root node
        HGRootNode rootNode = slizaaService.getRootNode();

        //
        if (rootNode != null) {
            return function.apply(rootNode);
        }

        //
        return null;
    }
}
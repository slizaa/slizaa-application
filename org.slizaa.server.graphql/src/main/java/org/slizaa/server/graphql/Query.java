package org.slizaa.server.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;
import org.slizaa.server.service.slizaa.ISlizaaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
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
     * @return
     */
    public Node rootNode() {
        return new Node(slizaaService.getRootNode());
    }

    /**
     * @param id
     * @return
     */
    public Node node(String id) {
        return new Node(slizaaService.getRootNode().lookupNode(Long.parseLong(id)));
    }

    public List<Node> nodes(List<String> ids) {
        return ids.stream().map(id -> node(id)).collect(Collectors.toList());
    }

    /**
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
}
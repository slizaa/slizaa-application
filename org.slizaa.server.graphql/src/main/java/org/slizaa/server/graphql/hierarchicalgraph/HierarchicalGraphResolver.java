package org.slizaa.server.graphql.hierarchicalgraph;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slizaa.hierarchicalgraph.core.algorithms.GraphUtils;
import org.slizaa.hierarchicalgraph.core.algorithms.IDependencyStructureMatrix;
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
    public List<Node> nodes(HierarchicalGraph hierarchicalGraph, List<String> ids) {
        return ids.stream().map(id -> node(hierarchicalGraph, id)).filter(node -> node != null).collect(Collectors.toList());
    }

    public List<Node> referencedNodes(HierarchicalGraph hierarchicalGraph, List<String> ids, boolean includePredecessors) {
        return computeReferencedNode(hierarchicalGraph, ids, includePredecessors, hgNode -> new Node(hgNode));
    }

    public List<String> referencedNodeIds(HierarchicalGraph hierarchicalGraph, List<String> ids, boolean includePredecessors) {
        return computeReferencedNode(hierarchicalGraph, ids, includePredecessors, hgNode -> hgNode.getIdentifier().toString());
    }

    /**
     * @param ids
     * @return
     */
    public DependencyMatrix dependencyMatrix(HierarchicalGraph hierarchicalGraph, List<String> ids) {

    	//
    	checkNotNull(ids);
    	
    	//
    	List<HGNode> hgNodes = nullSafe(hierarchicalGraph, rootNode -> {
    		return ids.stream().map(id -> rootNode.lookupNode(Long.parseLong(id))).filter(node -> node != null).collect(Collectors.toList());
    	});
    	
    	//
    	IDependencyStructureMatrix dependencyStructureMatrix = GraphUtils.createDependencyStructureMatrix(hgNodes);
    	
    	//
        List<Node> orderedNodes = dependencyStructureMatrix.getOrderedNodes().stream().map(hgNode -> new Node(hgNode)).collect(Collectors.toList());

        //
        return new DependencyMatrix(orderedNodes, dependencyStructureMatrix.getMatrix());
    }

    private <T> List<T> computeReferencedNode(HierarchicalGraph hierarchicalGraph, List<String> ids, boolean includePredecessors, Function<HGNode, T> mappingFunction) {

        //
        Stream<HGNode> nodeStream = ids.stream().map(id -> node(hierarchicalGraph, id)).
                flatMap(node -> node.getHgNode().getAccumulatedOutgoingCoreDependencies().stream()).
                map(dep -> dep.getTo());

        if (includePredecessors) {
            nodeStream = nodeStream.flatMap(node -> Stream.concat(node.getPredecessors().stream(), Stream.of(node)));
        }

        return  nodeStream.distinct().map(hgNode -> mappingFunction.apply(hgNode)).collect(Collectors.toList());
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

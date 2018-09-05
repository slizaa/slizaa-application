package org.slizaa.server.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ws.rs.*;

import com.google.gson.internal.$Gson$Preconditions;
import org.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;
import org.slizaa.hierarchicalgraph.core.model.HGNode;
import org.slizaa.hierarchicalgraph.core.model.HGRootNode;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;
import org.slizaa.server.SlizaaComponent;
import org.slizaa.server.rest.model.AggregatedDependencies;
import org.slizaa.server.rest.model.AggregatedDependency;
import org.slizaa.server.rest.model.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 *
 * </p>
 * <p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
@Component
@Path("/rest")
public class HierarchicalTreeController {

  /**
   * the slizaa component
   */
  @Autowired
  private SlizaaComponent _component;

  /**
   * <p>
   * Returns the root node of the application.
   * </p>
   *
   * @return the root node of the application.
   */
  @GET
  @Path("/root")
  @Produces("application/json")
  public Node root() {

    //
    return mapNode(this._component.getRootNode(), true);
  }

  /**
   * <p>
   * </p>
   * http://localhost:8080/rest/node/28
   *
   * @param nodeId
   * @return
   */
  @GET
  @Path("/node/{id}")
  @Produces("application/json")
  public Node node(@PathParam("id") long nodeId,
      @DefaultValue("false")
      @QueryParam(value = "resolveChildren") String resolveChildren) {

    //
    HGNode hgNode = this._component.getRootNode().lookupNode(nodeId);

    //
    if (hgNode == null) {
      throw new NotFoundException(String.format("Node '%s' does not exist in the hierarchical graph.", nodeId));
    }

    //
    return mapNode(hgNode, Boolean.valueOf(resolveChildren));
  }

  /**
   * <p>
   *
   * </p>
   * @param fromIds
   * @param toIds
   * @return
   */
   @GET
  @Path("/node/{id}/outgoingDependencies/{to}")
  @Produces("application/json")
  public AggregatedDependencies outgoingDependencies(@PathParam("id") String fromIds, @PathParam("to") String toIds) {

    //
    return getAggregatedDependencies(fromIds, toIds, (sourceNode, targetNodes) -> sourceNode.getOutgoingDependenciesTo(targetNodes));
  }

  /**
   * <p>
   *
   * </p>
   * @param nodeIds
   * @param fromIds
   * @return
   */
  @GET
  @Path("/node/{id}/incomingDependencies/{from}")
  @Produces("application/json")
  public AggregatedDependencies incomingDependencies(@PathParam("id") String nodeIds,
      @PathParam("from") String fromIds) {

    //
    return getAggregatedDependencies(nodeIds, fromIds, (sourceNode, targetNodes) -> sourceNode.getIncomingDependenciesFrom(targetNodes));
  }

  /**
   * <p>
   * Maps the given {@link HGNode} to a DTO.
   * </p>
   *
   * @param node
   * @return
   */
  private Node mapNode(HGNode node, boolean resolveChildren) {

    //
    checkNotNull(node);

    //
    ILabelDefinitionProvider labelDefinitionProvider = checkNotNull(this._component.getRootNode()
        .getExtension(ILabelDefinitionProvider.class));

    //
    List<Node> children = resolveChildren ?
        node.getChildren().stream().map(hgNode -> mapNode(hgNode, false)).collect(Collectors.toList()) :
        Collections.emptyList();

    //
    return new Node((long) node.getIdentifier(), !resolveChildren && !node.getChildren().isEmpty(),
        labelDefinitionProvider.getLabelDefinition(node).getText(),
        children);
  }

  /**
   * <p>
   * </p>
   *
   * @param sourceIds
   * @param targetIds
   * @return
   */
  private AggregatedDependencies getAggregatedDependencies(String sourceIds, String targetIds, BiFunction<HGNode, List<HGNode>, List<HGAggregatedDependency>> dependencyResolver) {

    //
    List<Object> splitSourceIds = Arrays.stream(sourceIds.split(",")).collect(Collectors.toList());
    List<Object> splitTargetIds = Arrays.stream(targetIds.split(",")).collect(Collectors.toList());

    //
    HGRootNode rootNode = this._component.getRootNode();

    //
    List<HGNode> sourceNodes = splitSourceIds.stream().map(id -> rootNode.lookupNode(Long.parseLong(id.toString())))
        .collect(Collectors.toList());

    //
    List<HGNode> targetNodes = splitTargetIds.stream().map(id -> rootNode.lookupNode(Long.parseLong(id.toString())))
        .collect(Collectors.toList());

    //
    List<HGAggregatedDependency> aggregatedDependencies = new ArrayList<>();

    //
    for (HGNode sourceNode : sourceNodes) {
      aggregatedDependencies.addAll(dependencyResolver.apply(sourceNode, targetNodes));
    }

    //
    // // TODO: handle null
    //
    // HGAggregatedDependency dependency = fromNode.getOutgoingDependenciesTo(toNode);

    //
    return new AggregatedDependencies(
        aggregatedDependencies.stream().map(dep -> new AggregatedDependency(dep)).collect(Collectors.toList()));
  }
}

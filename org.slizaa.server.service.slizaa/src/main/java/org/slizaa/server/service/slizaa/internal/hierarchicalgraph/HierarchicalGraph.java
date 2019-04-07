package org.slizaa.server.service.slizaa.internal.hierarchicalgraph;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.function.Function;

import org.slizaa.core.progressmonitor.DefaultProgressMonitor;
import org.slizaa.hierarchicalgraph.core.model.HGRootNode;
import org.slizaa.hierarchicalgraph.graphdb.mapping.service.IMappingService;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IMappingProvider;
import org.slizaa.server.service.slizaa.IHierarchicalGraph;
import org.slizaa.server.service.slizaa.IHierarchicalGraphContainer;

/**
 * 
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
public class HierarchicalGraph implements IHierarchicalGraph {

  private HierarchicalGraphDefinition                       _hierarchicalGraphDefinition;

  private IHierarchicalGraphContainer                       _container;

  private HGRootNode                                        _rootNode;

  private Function<HierarchicalGraphDefinition, HGRootNode> _creatorFunction;

  /**
   * 
   * @param hierarchicalGraphDefinition
   * @param container
   */
  public HierarchicalGraph(HierarchicalGraphDefinition hierarchicalGraphDefinition,
      IHierarchicalGraphContainer container, Function<HierarchicalGraphDefinition, HGRootNode> creatorFunction) {

    _hierarchicalGraphDefinition = checkNotNull(hierarchicalGraphDefinition);
    _container = checkNotNull(container);
    _creatorFunction = checkNotNull(creatorFunction);
  }

  public HGRootNode getRootNode() {
    return this._rootNode;
  }

  @Override
  public IHierarchicalGraphContainer getContainer() {
    return _container;
  }

  @Override
  public String getIdentifier() {
    return _hierarchicalGraphDefinition.getIdentifier();
  }

  public void initialize() {
    _rootNode= _creatorFunction.apply(_hierarchicalGraphDefinition);
  }
  
  public void dispose() {
    _rootNode = null;
  }
}

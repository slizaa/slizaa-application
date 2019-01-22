package org.slizaa.server.service.slizaa.internal.hierarchicalgraph;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slizaa.hierarchicalgraph.core.model.HGRootNode;
import org.slizaa.server.service.slizaa.IHierarchicalGraph;
import org.slizaa.server.service.slizaa.IHierarchicalGraphContainer;

public class HierarchicalGraph implements IHierarchicalGraph {

	private HGRootNode _rootNode;

	private IHierarchicalGraphContainer _container;

	private String _identifier;

	public HierarchicalGraph(String identifier, HGRootNode rootNode, IHierarchicalGraphContainer container) {
		_container = checkNotNull(container);
		_rootNode = checkNotNull(rootNode);
		_identifier = checkNotNull(identifier);
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
		return _identifier;
	}
}

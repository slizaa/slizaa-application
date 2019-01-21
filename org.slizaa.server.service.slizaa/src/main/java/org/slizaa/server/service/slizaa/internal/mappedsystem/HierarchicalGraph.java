package org.slizaa.server.service.slizaa.internal.mappedsystem;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slizaa.core.boltclient.IBoltClient;
import org.slizaa.hierarchicalgraph.core.model.HGRootNode;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;
import org.slizaa.server.service.slizaa.IHierarchicalGraph;
import org.slizaa.server.service.slizaa.IHierarchicalGraphContainer;

public class HierarchicalGraph implements IHierarchicalGraph {

	//
	private ILabelDefinitionProvider _labelDefinitionProvider;

	private IBoltClient _boltClient;

	private HGRootNode _rootNode;

	private IHierarchicalGraphContainer _container;

	private String _identifier;

	/**
	 * 
	 * @param container
	 * @param identifier
	 */
	public HierarchicalGraph(IHierarchicalGraphContainer container, String identifier) {
		_container = checkNotNull(container);
		_identifier = checkNotNull(identifier);
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @return
	 */
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

//  /**
//   * <p>
//   * </p>
//   *
//   * @throws Exception
//   */
//  public void mapSystem() throws Exception {
//    // this._boltClient.disconnect();
//    //
//    IBoltClientFactory boltClientFactory = IBoltClientFactory.newInstance(this._slizaaComponent.getExecutorService());
//    // TODO!!
//    this._boltClient = boltClientFactory.createBoltClient("bolt://localhost:5001");
//    this._boltClient.connect();
//
//    //
//    IMappingService mappingService = IMappingService.createHierarchicalgraphMappingService();
//    // TODO!!
//    IMappingProvider mappingProvider = this._slizaaComponent.getSlizaaServerBackend().getMappingProviders().get(0);
//
//    //
//    this._rootNode = mappingService.convert(mappingProvider, this._boltClient,
//        new DefaultProgressMonitor("Mapping", 100, DefaultProgressMonitor.consoleLogger()));
//
//    //
//    _labelDefinitionProvider = mappingProvider.getLabelDefinitionProvider();
//  }
}

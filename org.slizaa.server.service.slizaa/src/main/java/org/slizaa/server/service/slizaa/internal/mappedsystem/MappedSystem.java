package org.slizaa.server.service.slizaa.internal.mappedsystem;

import org.slizaa.core.boltclient.IBoltClient;
import org.slizaa.hierarchicalgraph.core.model.HGRootNode;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;
import org.slizaa.server.service.slizaa.IMappedSystem;

public class MappedSystem implements IMappedSystem {

  //
  private ILabelDefinitionProvider _labelDefinitionProvider;

  private IBoltClient _boltClient;

  private HGRootNode _rootNode;

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public HGRootNode getRootNode() {
    return this._rootNode;
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

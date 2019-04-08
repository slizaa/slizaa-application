package org.slizaa.server.service.slizaa.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slizaa.core.boltclient.IBoltClientFactory;
import org.slizaa.hierarchicalgraph.graphdb.mapping.service.IMappingService;
import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProviderFactory;
import org.slizaa.server.service.backend.IBackendService;
import org.slizaa.server.service.backend.IBackendServiceInstanceProvider;
import org.slizaa.server.service.configuration.IConfigurationService;
import org.slizaa.server.service.extensions.IExtensionService;
import org.slizaa.server.service.slizaa.IGraphDatabase;
import org.slizaa.server.service.slizaa.ISlizaaService;
import org.slizaa.server.service.slizaa.internal.configuration.GraphDatabaseConfiguration;
import org.slizaa.server.service.slizaa.internal.configuration.GraphDatabaseHierarchicalGraphConfiguration;
import org.slizaa.server.service.slizaa.internal.configuration.SlizaaServiceConfiguration;
import org.slizaa.server.service.slizaa.internal.graphdatabase.GraphDatabaseFactory;
import org.slizaa.server.service.slizaa.internal.graphdatabase.SlizaaSocketUtils;
import org.slizaa.server.service.svg.ISvgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
@Component
public class SlizaaServiceImpl implements ISlizaaService {

  {
    org.slizaa.hierarchicalgraph.core.model.CustomFactoryStandaloneSupport.registerCustomHierarchicalgraphFactory();
    org.slizaa.hierarchicalgraph.graphdb.model.CustomFactoryStandaloneSupport.registerCustomHierarchicalgraphFactory();
  }

  private static final String                       CONFIG_ID           = "org.slizaa.server.service.slizaa";

  private static final Logger                       logger              = LoggerFactory
      .getLogger(SlizaaServiceImpl.class);

  @Autowired
  private SlizaaServiceDatabaseProperties           _serviceProperties;

  @Autowired
  private IBackendServiceInstanceProvider           _backendService;

  @Autowired
  private IExtensionService                         _extensionService;

  @Autowired
  private IConfigurationService                     _configurationService;

  @Autowired
  private ISvgService                               _svgService;

  @Autowired
  private GraphDatabaseFactory                      _graphDatabaseFactory;

  @Autowired
  private ContentDefinitionProviderFactoryService   _contentDefinitionProviderFactoryService;

  @Autowired
  private IMappingService                           _mappingService;

  private ExecutorService                           _executorService;

  private ConcurrentHashMap<String, IGraphDatabase> _structureDatabases = new ConcurrentHashMap<>();

  private IBoltClientFactory                        _boltClientFactory;

  /**
   * <p>
   * </p>
   */
  @PostConstruct
  public void initialize() throws Exception {

    // TODO: config!
    this._executorService = Executors.newFixedThreadPool(20);
    _boltClientFactory = IBoltClientFactory.newInstance(this._executorService);

    SlizaaServiceConfiguration configuration = _configurationService.load(CONFIG_ID, SlizaaServiceConfiguration.class);

    if (configuration != null) {

      for (GraphDatabaseConfiguration dbConfig : configuration.getGraphDatabases()) {

        try {

          // create
          IGraphDatabase graphDatabase = createStructureDatabaseIfAbsent(dbConfig.getIdentifier(), dbConfig.getPort());

          //
          if (dbConfig.getContentDefinition() != null) {
            graphDatabase.setContentDefinition(dbConfig.getContentDefinition().getFactoryId(),
                dbConfig.getContentDefinition().getContentDefinition());
          }

          // and start
          if (dbConfig.isRunning()) {
            graphDatabase.start();
          }

          //
          for (GraphDatabaseHierarchicalGraphConfiguration hierarchicalGraphCfg : dbConfig.getHierarchicalGraphs()) {
            graphDatabase.newHierarchicalGraph(hierarchicalGraphCfg.getIdentifier());
          }

        } catch (Exception e) {

          // TODO Auto-generated catch block
          e.printStackTrace();
          _structureDatabases.remove(dbConfig.getIdentifier());
        }

      }
    }
  }

  @PreDestroy
  public void dispose() throws InterruptedException {

    this._executorService.shutdown();
    this._executorService.awaitTermination(5, TimeUnit.SECONDS);
  }

  @Override
  public IExtensionService getExtensionService() {
    return _extensionService;
  }

  @Override
  public IBackendService getBackendService() {
    return _backendService;
  }

  @Override
  public ISvgService getSvgService() {
    return _svgService;
  }

  @Override
  public boolean hasStructureDatabases() {
    return _structureDatabases != null;
  }

  @Override
  public List<? extends IGraphDatabase> getGraphDatabases() {

    //
    List<? extends IGraphDatabase> result = new ArrayList<>(_structureDatabases.values());
    result.sort(new Comparator<IGraphDatabase>() {
      @Override
      public int compare(IGraphDatabase o1, IGraphDatabase o2) {
        return o1.getIdentifier().compareTo(o2.getIdentifier());
      }
    });
    return result;
  }

  @Override
  public IGraphDatabase getGraphDatabase(String identifier) {
    return _structureDatabases.get(identifier);
  }

  @Override
  public IGraphDatabase newGraphDatabase(String identifier) {

    //
    if (_structureDatabases.containsKey(identifier)) {
      // TODO
      throw new RuntimeException();
    }

    //
    storeConfig();

    //
    return createStructureDatabaseIfAbsent(identifier, SlizaaSocketUtils.findAvailableTcpPort());
  }

  public void storeConfig() {

    SlizaaServiceConfiguration configuration = new SlizaaServiceConfiguration();

    for (IGraphDatabase graphDatabase : _structureDatabases.values()) {
      configuration.getGraphDatabases().add(new GraphDatabaseConfiguration(graphDatabase));
    }

    // save the config
    try {
      _configurationService.store(CONFIG_ID, configuration);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public boolean hasStructureDatabase(String identifier) {
    return _structureDatabases.containsKey(checkNotNull(identifier));
  }

  /**
   * 
   * @return
   */
  public IBackendServiceInstanceProvider getInstanceProvider() {
    return _backendService;
  }

  /**
   * 
   * @return
   */
  public IBoltClientFactory getBoltClientFactory() {
    return _boltClientFactory;
  }

  /**
   * 
   * @return
   */
  public IMappingService getMappingService() {
    return _mappingService;
  }

  public ConcurrentHashMap<String, IGraphDatabase> structureDatabases() {
    return _structureDatabases;
  }

  private IGraphDatabase createStructureDatabaseIfAbsent(String identifier, int port) {
    return _structureDatabases.computeIfAbsent(identifier, id -> _graphDatabaseFactory.newInstance(id,
        new File(_serviceProperties.getDatabaseRootDirectoryAsFile(), identifier), port, this));
  }

  public IContentDefinitionProviderFactory<?> getContentDefinitionProviderFactory(String contentDefinitionFactoryId) {
    return _contentDefinitionProviderFactoryService
        .getContentDefinitionProviderFactory(checkNotNull(contentDefinitionFactoryId));
  }
}

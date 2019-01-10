package org.slizaa.server.service.slizaa.internal;

import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slizaa.core.boltclient.IBoltClientFactory;
import org.slizaa.server.service.backend.ISlizaaServerBackend;
import org.slizaa.server.service.extensions.IExtension;
import org.slizaa.server.service.extensions.IExtensionIdentifier;
import org.slizaa.server.service.extensions.IExtensionService;
import org.slizaa.server.service.slizaa.ISlizaaService;
import org.slizaa.server.service.slizaa.IStructureDatabase;
import org.slizaa.server.service.slizaa.internal.structuredatabase.StructureDatabase;
import org.slizaa.server.service.slizaa.internal.structuredatabase.StructureDatabaseEvent;
import org.slizaa.server.service.slizaa.internal.structuredatabase.StructureDatabaseState;
import org.slizaa.server.service.slizaa.internal.structuredatabase.StructureDatabaseStateMachineAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
@Component
public class SlizaaServiceImpl implements ISlizaaService {

  //
  private static final Logger logger = LoggerFactory.getLogger(SlizaaServiceImpl.class);

  @Value("${slizaa.working.directory:}")
  private String _databaseDirectoryPath;


  @Autowired
  private StateMachineFactory<StructureDatabaseState, StructureDatabaseEvent> _stateMachineFactory;

  @Autowired
  private ISlizaaServerBackend _slizaaServerBackend;

  @Autowired
  private IExtensionService _extensionService;

  private File _databaseDirectory;

  private ExecutorService _executorService;

  //
  private ConcurrentHashMap<String, StructureDatabase> _structureDatabases = new ConcurrentHashMap<>();

  private IBoltClientFactory _boltClientFactory;

  {
    org.slizaa.hierarchicalgraph.core.model.CustomFactoryStandaloneSupport.registerCustomHierarchicalgraphFactory();
    org.slizaa.hierarchicalgraph.graphdb.model.CustomFactoryStandaloneSupport.registerCustomHierarchicalgraphFactory();
  }

  /**
   * <p>
   * </p>
   */
  @PostConstruct
  public void initialize() throws Exception {

    // TODO: config!
    this._executorService = Executors.newFixedThreadPool(20);
    _boltClientFactory = IBoltClientFactory.newInstance(this._executorService);
  }

  @PreDestroy
  public void dispose() throws InterruptedException {

    this._executorService.shutdown();
    this._executorService.awaitTermination(5, TimeUnit.SECONDS);
  }

  /**
   * @return
   */
  @Override
  public boolean isBackendConfigured() {
    return _slizaaServerBackend.isConfigured();
  }

  @Override
  public List<IExtension> getInstalledExtensions() {
    return _slizaaServerBackend.getInstalledExtensions();
  }

  @Override
  public IExtensionService getExtensionService() {
    return _extensionService;
  }

  /**
   * @param extensionIdentifiers
   * @return
   */
  @Override
  public List<IExtension> installExtensions(List<? extends IExtensionIdentifier> extensionIdentifiers) {

    // compute the extensions to install
    List<IExtension> extensionsToInstall = this._extensionService.getExtensions(extensionIdentifiers);
    List<IExtension> installedExtensions = this._slizaaServerBackend.getInstalledExtensions();
    List<IExtension> newExtensions = ListUtils.subtract(extensionsToInstall, installedExtensions);

    // install the new extension
    _slizaaServerBackend.installExtensions(newExtensions);

    // return the newly installed extensions
    return newExtensions;
  }

  @Override
  public List<IExtension> uninstallExtensions(List<? extends IExtensionIdentifier> extensionIds) {
    // TODO
    return Collections.emptyList();
  }

  @Override
  public boolean hasStructureDatabases() {
    return _structureDatabases != null;
  }

  @Override
  public List<? extends IStructureDatabase> getStructureDatabases() {

    //
    List<? extends IStructureDatabase> result = new ArrayList<>(_structureDatabases.values());
    result.sort(new Comparator<IStructureDatabase>() {
      @Override
      public int compare(IStructureDatabase o1, IStructureDatabase o2) {
        return o1.getIdentifier().compareTo(o2.getIdentifier());
      }
    });
    return result;
  }

  @Override
  public IStructureDatabase newStructureDatabase(String identifier) {

    // TODO: SAVE CONFIG

    //
    return _structureDatabases.computeIfAbsent(identifier, id -> {

      //
      return StructureDatabase.create(id, new File(_databaseDirectoryPath), this._slizaaServerBackend,
          this._boltClientFactory, () -> this._stateMachineFactory.getStateMachine());
    });
  }

  public void setDatabaseDirectory(File databaseDirectory) {
    _databaseDirectory = databaseDirectory;
  }
}

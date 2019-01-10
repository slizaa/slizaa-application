package org.slizaa.server.service.slizaa.internal.structuredatabase;

import org.slizaa.core.boltclient.IBoltClientFactory;
import org.slizaa.core.progressmonitor.DefaultProgressMonitor;
import org.slizaa.scanner.api.graphdb.IGraphDb;
import org.slizaa.scanner.api.importer.IModelImporter;
import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import org.slizaa.server.service.backend.ISlizaaServerBackend;
import org.slizaa.server.service.slizaa.IMappedSystem;
import org.slizaa.server.service.slizaa.IStructureDatabase;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public class StructureDatabase
    implements IInternalStructureDatabase {

  //
  private String _id;

  //
  private File _databaseDirectory;

  //
  private ISlizaaServerBackend _serverBackend;

  //
  private IBoltClientFactory _boltClientFactory;

  //
  private StateMachine<StructureDatabaseState, StructureDatabaseEvent> _stateMachine;

  //
  private IContentDefinitionProvider _contentDefinitionProvider;

  //
  protected IGraphDb _graphDb;

  /**
   *
   */
  private StructureDatabase(String id, File databaseDirectory, ISlizaaServerBackend serverBackend, IBoltClientFactory boltClientFactory, StateMachine<StructureDatabaseState, StructureDatabaseEvent> stateMachine) {
    this._id = checkNotNull(id);
    this._databaseDirectory = checkNotNull(databaseDirectory);
    this._serverBackend = checkNotNull(serverBackend);
    this._stateMachine = checkNotNull(stateMachine);
    this._boltClientFactory = checkNotNull(boltClientFactory);
  }

  @Override
  public String getIdentifier() {
    return _id;
  }

  /**
   * @param contentDefinitionProvider
   */
  @Override
  public void setContentDefinitionProvider(IContentDefinitionProvider contentDefinitionProvider) {
    this._contentDefinitionProvider = contentDefinitionProvider;
  }

  /**
   * @return
   */
  @Override
  public boolean hasContentDefinitionProvider() {
    return _contentDefinitionProvider != null;
  }

  @Override
  public IMappedSystem createNewMappedSystem(String identifier) {

    //
    if (!StructureDatabaseState.RUNNING.equals(this._stateMachine.getState())) {

    }

    return null;
  }

  @Override
  public void disposeMappedSystem(String identifier) {

  }

  @Override
  public List<IMappedSystem> getMappedSystems() {
    return null;
  }

  /**
   * <p>
   * </p>
   *
   * @throws IOException
   */
  public void parse(boolean startDatabase) throws IOException {
    this._stateMachine.sendEvent(StructureDatabaseEvent.PARSE);
  }

  @Override
  public void start() {
    this._stateMachine.sendEvent(StructureDatabaseEvent.START);
  }

  @Override
  public void stop() {
    this._stateMachine.sendEvent(StructureDatabaseEvent.STOP);
  }

  @Override
  public void _start() {
    // TODO: PORT
    _graphDb = _serverBackend.getGraphDbFactory().newGraphDb(5001, _databaseDirectory).create();
  }

  @Override
  public void _stop() {
    this._graphDb.shutdown();
  }

  /**
   *
   * @throws Exception
   */
  @Override
  public void _parse() {

    // delete all contained files
    try {
      Files.walk(_databaseDirectory.toPath(), FileVisitOption.FOLLOW_LINKS).sorted(Comparator.reverseOrder())
          .map(Path::toFile).forEach(File::delete);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // FUCK ME!
    Thread.currentThread().setContextClassLoader(_serverBackend.getCurrentExtensionClassLoader());

    IModelImporter modelImporter = _serverBackend.getModelImporterFactory()
        .createModelImporter(_contentDefinitionProvider,
            _databaseDirectory, _serverBackend.getParserFactories(),
            _serverBackend.getCypherStatementRegistry().getAllStatements());

    // parse the model
    if (true) {

      //
      modelImporter.parse(new DefaultProgressMonitor("Parse", 100, DefaultProgressMonitor.consoleLogger()),
          // TODO
          () -> _serverBackend.getGraphDbFactory().newGraphDb(5001, _databaseDirectory).create());

      //
      _graphDb = modelImporter.getGraphDb();
    }
    //
    else {
      modelImporter.parse(new DefaultProgressMonitor("Parse", 100, DefaultProgressMonitor.consoleLogger()));
    }
  }

  public static StructureDatabase create(String identifier, File databaseParentDirectoryPath, ISlizaaServerBackend serverBackend,
      IBoltClientFactory boltClientFactory, Supplier<StateMachine<StructureDatabaseState, StructureDatabaseEvent>> stateMachineSupplier) {

    checkNotNull(identifier);
    checkNotNull(databaseParentDirectoryPath);
    checkNotNull(serverBackend);
    checkNotNull(boltClientFactory);
    checkNotNull(stateMachineSupplier);

    //
    File databaseDirectory = new File(databaseParentDirectoryPath, identifier);
    if (!databaseDirectory.exists()) {
      databaseDirectory.mkdirs();
    }

    // create the state machine
    StateMachine<StructureDatabaseState, StructureDatabaseEvent> stateMachine = stateMachineSupplier.get();

    // create the structure database
    StructureDatabase structureDatabase = new StructureDatabase(identifier, databaseDirectory, serverBackend, boltClientFactory, stateMachine);

    // connect the structure database to the state machine
    stateMachine.addStateListener(new StructureDatabaseStateMachineAdapter(structureDatabase));

    // finally return the result
    return structureDatabase;

  }
}

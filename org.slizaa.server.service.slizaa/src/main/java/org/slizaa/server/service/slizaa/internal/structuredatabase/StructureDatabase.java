package org.slizaa.server.service.slizaa.internal.structuredatabase;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import org.slizaa.core.boltclient.IBoltClientFactory;
import org.slizaa.core.progressmonitor.DefaultProgressMonitor;
import org.slizaa.scanner.api.graphdb.IGraphDb;
import org.slizaa.scanner.api.importer.IModelImporter;
import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import org.slizaa.server.service.backend.IBackendServiceInstanceProvider;
import org.slizaa.server.service.slizaa.IHierarchicalGraph;
import org.slizaa.server.service.slizaa.IStructureDatabase;
import org.springframework.statemachine.StateMachine;

/**
 *
 */
public class StructureDatabase implements IStructureDatabase {

	//
	private String _id;

	//
	private File _databaseDirectory;

	//
	private IBackendServiceInstanceProvider _serverBackend;

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
	private StructureDatabase(String id, File databaseDirectory, IBackendServiceInstanceProvider serverBackend,
			IBoltClientFactory boltClientFactory,
			StateMachine<StructureDatabaseState, StructureDatabaseEvent> stateMachine) {
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
	public IHierarchicalGraph createNewHierarchicalGraph(String identifier) {

		//
		checkState(StructureDatabaseState.RUNNING.equals(this._stateMachine.getState().getId()), "Database is not running.");

		//
		return null;
	}

	@Override
	public void disposeHierarchicalGraph(String identifier) {

	}

	@Override
	public List<IHierarchicalGraph> getHierarchicalGraphs() {
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

	public boolean _isRunning() {
		return _graphDb != null;
	}
	
	public void _start() {
		// TODO: PORT
		_graphDb = _serverBackend.getGraphDbFactory().newGraphDb(5001, _databaseDirectory).create();
	}

	public void _stop() {
		this._graphDb.shutdown();
		this._graphDb = null;
	}

	public void _shutdown() {
		this._graphDb.shutdown();
		this._graphDb = null;
	}

	/**
	 *
	 * @throws Exception
	 */
	public boolean _parse() {

		// delete all contained files
		try {
			Files.walk(_databaseDirectory.toPath(), FileVisitOption.FOLLOW_LINKS).sorted(Comparator.reverseOrder())
					.map(Path::toFile).forEach(File::delete);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// FUCK ME!
		Thread.currentThread().setContextClassLoader(_serverBackend.getCurrentExtensionClassLoader());

		IModelImporter modelImporter = _serverBackend.getModelImporterFactory().createModelImporter(
				_contentDefinitionProvider, _databaseDirectory, _serverBackend.getParserFactories(),
				_serverBackend.getCypherStatementRegistry().getAllStatements());

		// parse the model
		if (true) {

			//
			modelImporter.parse(new DefaultProgressMonitor("Parse", 100, DefaultProgressMonitor.consoleLogger()),
					// TODO
					() -> _serverBackend.getGraphDbFactory().newGraphDb(5001, _databaseDirectory).create());

			_graphDb = modelImporter.getGraphDb();

			return true;
		}
		//
		else {
			modelImporter.parse(new DefaultProgressMonitor("Parse", 100, DefaultProgressMonitor.consoleLogger()));
			return false;
		}
	}

//	public static StructureDatabase create(String identifier, File databaseParentDirectoryPath,
//			IBackendServiceInstanceProvider serverBackend, IBoltClientFactory boltClientFactory,
//			Supplier<StateMachine<StructureDatabaseState, StructureDatabaseEvent>> stateMachineSupplier) {
//
//		checkNotNull(identifier);
//		checkNotNull(databaseParentDirectoryPath);
//		checkNotNull(serverBackend);
//		checkNotNull(boltClientFactory);
//		checkNotNull(stateMachineSupplier);
//
//		//
//		File databaseDirectory = new File(databaseParentDirectoryPath, identifier);
//		if (!databaseDirectory.exists()) {
//			databaseDirectory.mkdirs();
//		}
//
//		// create the state machine
//		StateMachine<StructureDatabaseState, StructureDatabaseEvent> stateMachine = stateMachineSupplier.get();
//
//		// create the structure database
//		StructureDatabase structureDatabase = new StructureDatabase(identifier, databaseDirectory, serverBackend,
//				boltClientFactory, stateMachine);
//
//		// finally return the result
//		return structureDatabase;
//
//	}
}

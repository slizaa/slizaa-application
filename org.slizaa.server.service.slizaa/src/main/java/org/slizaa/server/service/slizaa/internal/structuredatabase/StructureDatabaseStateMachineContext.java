package org.slizaa.server.service.slizaa.internal.structuredatabase;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import org.slizaa.core.boltclient.IBoltClientFactory;
import org.slizaa.core.progressmonitor.DefaultProgressMonitor;
import org.slizaa.scanner.api.graphdb.IGraphDb;
import org.slizaa.scanner.api.importer.IModelImporter;
import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import org.slizaa.server.service.backend.IBackendServiceInstanceProvider;
import org.springframework.statemachine.StateMachine;

public class StructureDatabaseStateMachineContext {

	private String _id;
	private File _databaseDirectory;
	private IBackendServiceInstanceProvider _serverBackend;
	private IBoltClientFactory _boltClientFactory;
	
	private IContentDefinitionProvider _contentDefinitionProvider;
	private IGraphDb _graphDb;
	
	StructureDatabaseStateMachineContext(String id, File databaseDirectory, IBackendServiceInstanceProvider serverBackend,
			IBoltClientFactory boltClientFactory) {
		this._id = checkNotNull(id);
		this._databaseDirectory = checkNotNull(databaseDirectory);
		this._serverBackend = checkNotNull(serverBackend);
		this._boltClientFactory = checkNotNull(boltClientFactory);
	}
	
	public String getIdentifier() {
		return _id;
	}
	
	public void setContentDefinitionProvider(IContentDefinitionProvider contentDefinitionProvider) {
		this._contentDefinitionProvider = contentDefinitionProvider;
	}

	/**
	 * @return
	 */
	public boolean hasContentDefinitionProvider() {
		return _contentDefinitionProvider != null;
	}
	
	public boolean isRunning() {
		return _graphDb != null;
	}
	
	public void start() {
		// TODO: PORT
		_graphDb = _serverBackend.getGraphDbFactory().newGraphDb(5001, _databaseDirectory).create();
	}

	public void stop() {
		this._graphDb.shutdown();
		this._graphDb = null;
	}

	public void shutdown() {
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

	public boolean hasPopulatedDatabaseDirectory() {
		return _databaseDirectory.isDirectory() && _databaseDirectory.list().length > 0;
	}
}

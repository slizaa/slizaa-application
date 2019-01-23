package org.slizaa.server.service.slizaa.internal.structuredatabase;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slizaa.core.boltclient.IBoltClient;
import org.slizaa.core.progressmonitor.DefaultProgressMonitor;
import org.slizaa.hierarchicalgraph.core.model.HGRootNode;
import org.slizaa.hierarchicalgraph.graphdb.mapping.service.IMappingService;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IMappingProvider;
import org.slizaa.scanner.api.graphdb.IGraphDb;
import org.slizaa.scanner.api.importer.IModelImporter;
import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import org.slizaa.server.service.slizaa.IHierarchicalGraph;
import org.slizaa.server.service.slizaa.internal.SlizaaServiceImpl;
import org.slizaa.server.service.slizaa.internal.hierarchicalgraph.HierarchicalGraph;

public class StructureDatabaseStateMachineContext {

	/** the id of the structure database */
	private String _id;

	/** the database directory */
	private File _databaseDirectory;
	private StructureDatabaseImpl _structureDatabase;
	private SlizaaServiceImpl _slizaaService;

	private IContentDefinitionProvider _contentDefinitionProvider;
	private IGraphDb _graphDb;
	private IBoltClient _boltClient;

	private Map<String, HierarchicalGraph> _map;

	StructureDatabaseStateMachineContext(String id, File databaseDirectory, SlizaaServiceImpl slizaaService) {

		this._id = checkNotNull(id);
		this._databaseDirectory = checkNotNull(databaseDirectory);
		this._slizaaService = checkNotNull(slizaaService);

		//
		_map = new HashMap<>();
	}

	void setStructureDatabase(StructureDatabaseImpl structureDatabase) {
		_structureDatabase = structureDatabase;
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
		_graphDb = _slizaaService.getInstanceProvider().getGraphDbFactory().newGraphDb(5001, _databaseDirectory)
				.create();
	}

	public void stop() {

		if (this._boltClient != null) {
			this._boltClient.disconnect();
			this._boltClient = null;
		}
		if (this._graphDb != null) {
			this._graphDb.shutdown();
			this._graphDb = null;
		}
	}

	public void terminate() {
		stop();
		_slizaaService.structureDatabases().remove(_id);
		clearDatabaseDirectory();
	}

	/**
	 * @throws Exception
	 */
	public boolean parse() {

		// delete all contained files
		clearDatabaseDirectory();

		// FUCK ME!
		Thread.currentThread()
				.setContextClassLoader(_slizaaService.getInstanceProvider().getCurrentExtensionClassLoader());

		IModelImporter modelImporter = _slizaaService.getInstanceProvider().getModelImporterFactory()
				.createModelImporter(_contentDefinitionProvider, _databaseDirectory,
						_slizaaService.getInstanceProvider().getParserFactories(),
						_slizaaService.getInstanceProvider().getCypherStatementRegistry().getAllStatements());

		// parse the model
		if (true) {

			//
			modelImporter.parse(new DefaultProgressMonitor("Parse", 100, DefaultProgressMonitor.consoleLogger()),
					// TODO
					() -> _slizaaService.getInstanceProvider().getGraphDbFactory().newGraphDb(5001, _databaseDirectory)
							.create());

			_graphDb = modelImporter.getGraphDb();

			System.out.println("******************* DONE ***********************");
			return true;
		}
		//
		else {
			modelImporter.parse(new DefaultProgressMonitor("Parse", 100, DefaultProgressMonitor.consoleLogger()));
			System.out.println("******************* DONE ***********************");
			return false;
		}


	}

	private void clearDatabaseDirectory() {
		try {
			Files.walk(_databaseDirectory.toPath(), FileVisitOption.FOLLOW_LINKS).sorted(Comparator.reverseOrder())
					.map(Path::toFile).forEach(File::delete);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean hasPopulatedDatabaseDirectory() {
		return _databaseDirectory.isDirectory() && _databaseDirectory.list().length > 0;
	}

	/**
	 * 
	 * @param identifier
	 * @return
	 */
	public IHierarchicalGraph createHierarchicalGraph(String identifier) {

		if (_boltClient == null) {
			// TODO!
			this._boltClient = _slizaaService.getBoltClientFactory().createBoltClient("bolt://localhost:5001");
			this._boltClient.connect();
		}

		// TODO: Spring?
		IMappingService mappingService = IMappingService.createHierarchicalgraphMappingService();

		// TODO!
		IMappingProvider mappingProvider = _slizaaService.getInstanceProvider().getMappingProviders().get(0);

		//
		HGRootNode rootNode = mappingService.convert(mappingProvider, this._boltClient,
				new DefaultProgressMonitor("Mapping", 100, DefaultProgressMonitor.consoleLogger()));

//		//
//		_labelDefinitionProvider = mappingProvider.getLabelDefinitionProvider();

		HierarchicalGraph hierarchicalGraph = new HierarchicalGraph(identifier, rootNode, _structureDatabase);

		_map.put(identifier, hierarchicalGraph);

		return hierarchicalGraph;
	}

	public void disposeHierarchicalGraph(String identifier) {

	}

	public List<IHierarchicalGraph> getHierarchicalGraphs() {
		return new ArrayList<>(_map.values());
	}
}

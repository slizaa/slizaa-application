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
import org.slizaa.server.service.backend.IBackendService;
import org.slizaa.server.service.backend.IBackendServiceInstanceProvider;
import org.slizaa.server.service.configuration.IConfigurationService;
import org.slizaa.server.service.extensions.IExtensionService;
import org.slizaa.server.service.slizaa.ISlizaaService;
import org.slizaa.server.service.slizaa.IStructureDatabase;
import org.slizaa.server.service.slizaa.internal.structuredatabase.StructureDatabase;
import org.slizaa.server.service.slizaa.internal.structuredatabase.StructureDatabaseEvent;
import org.slizaa.server.service.slizaa.internal.structuredatabase.StructureDatabaseState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
@Component
public class SlizaaServiceImpl implements ISlizaaService {

	private static final String CONFIG_ID = "org.slizaa.server.service.slizaa";

	private static final Logger logger = LoggerFactory.getLogger(SlizaaServiceImpl.class);

	@Value("${slizaa.working.directory:}")
	private String _databaseDirectoryPath;

	@Autowired
	private StateMachineFactory<StructureDatabaseState, StructureDatabaseEvent> _stateMachineFactory;

	@Autowired
	private IBackendServiceInstanceProvider _backendService;

	@Autowired
	private IExtensionService _extensionService;

	@Autowired
	private IConfigurationService _configurationService;

	private File _databaseDirectory;

	private ExecutorService _executorService;

	private ConcurrentHashMap<String, StructureDatabase> _structureDatabases = new ConcurrentHashMap<>();

	private IBoltClientFactory _boltClientFactory;

	private Configuration _configuration;

	{
		org.slizaa.hierarchicalgraph.core.model.CustomFactoryStandaloneSupport.registerCustomHierarchicalgraphFactory();
		org.slizaa.hierarchicalgraph.graphdb.model.CustomFactoryStandaloneSupport
				.registerCustomHierarchicalgraphFactory();
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

		_configuration = _configurationService.load(CONFIG_ID, Configuration.class);
		if (_configuration == null) {
			_configuration = new Configuration();
		}

		for (String identifier : _configuration.getStructureDatabases()) {
			_structureDatabases.computeIfAbsent(identifier, id -> {
				return StructureDatabase.create(id, new File(_databaseDirectoryPath), this._backendService,
						this._boltClientFactory, () -> this._stateMachineFactory.getStateMachine());
			});
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
	public IStructureDatabase getStructureDatabase(String identifier) {
		return _structureDatabases.get(identifier);
	}

	@Override
	public IStructureDatabase newStructureDatabase(String identifier) {

		//
		if (_structureDatabases.containsKey(identifier)) {
			// TODO
			throw new RuntimeException();
		}

		// save the config
		_configuration.getStructureDatabases().add(identifier);
		try {
			_configurationService.store(CONFIG_ID, _configuration);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//
		return _structureDatabases.computeIfAbsent(identifier, id -> {

			//
			return StructureDatabase.create(id, new File(_databaseDirectoryPath), this._backendService,
					this._boltClientFactory, () -> this._stateMachineFactory.getStateMachine());
		});
	}

	public void setDatabaseDirectory(File databaseDirectory) {
		_databaseDirectory = databaseDirectory;
	}

	public boolean hasStructureDatabase(String identifier) {
		return _structureDatabases.containsKey(checkNotNull(identifier));
	}
}

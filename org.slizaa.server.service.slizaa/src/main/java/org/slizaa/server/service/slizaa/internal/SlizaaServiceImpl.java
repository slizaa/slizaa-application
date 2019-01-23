package org.slizaa.server.service.slizaa.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
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
import org.slizaa.server.service.slizaa.internal.structuredatabase.StructureDatabaseFactory;
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
		org.slizaa.hierarchicalgraph.graphdb.model.CustomFactoryStandaloneSupport
				.registerCustomHierarchicalgraphFactory();
	}

	private static final String CONFIG_ID = "org.slizaa.server.service.slizaa";

	private static final Logger logger = LoggerFactory.getLogger(SlizaaServiceImpl.class);

	@Autowired
	private SlizaaServiceDatabaseProperties _serviceProperties;

	@Autowired
	private IBackendServiceInstanceProvider _backendService;

	@Autowired
	private IExtensionService _extensionService;

	@Autowired
	private IConfigurationService _configurationService;

	@Autowired
	private StructureDatabaseFactory _structureDatabaseFactory;

	private ExecutorService _executorService;

	private ConcurrentHashMap<String, IStructureDatabase> _structureDatabases = new ConcurrentHashMap<>();

	private IBoltClientFactory _boltClientFactory;

	private SlizaaServiceConfiguration _configuration;

	/**
	 * <p>
	 * </p>
	 */
	@PostConstruct
	public void initialize() throws Exception {

		// TODO: config!
		this._executorService = Executors.newFixedThreadPool(20);
		_boltClientFactory = IBoltClientFactory.newInstance(this._executorService);

		_configuration = _configurationService.load(CONFIG_ID, SlizaaServiceConfiguration.class);
		if (_configuration == null) {
			_configuration = new SlizaaServiceConfiguration();
		}

		for (String identifier : _configuration.getStructureDatabases()) {
			createStructureDatabaseIfAbsent(identifier);
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
//		_configuration.getStructureDatabases().add(identifier);
//		try {
//			_configurationService.store(CONFIG_ID, _configuration);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		//
		return createStructureDatabaseIfAbsent(identifier);
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

	public ConcurrentHashMap<String, IStructureDatabase> structureDatabases() {
		return _structureDatabases;
	}

	private IStructureDatabase createStructureDatabaseIfAbsent(String identifier) {
		return _structureDatabases.computeIfAbsent(identifier, id -> _structureDatabaseFactory.newInstance(id,
				new File(_serviceProperties.getDatabaseRootDirectoryAsFile(), identifier), this));
	}
}

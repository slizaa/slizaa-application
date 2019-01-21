package org.slizaa.server.service.backend.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IMappingProvider;
import org.slizaa.scanner.api.cypherregistry.ICypherStatementRegistry;
import org.slizaa.scanner.api.graphdb.IGraphDbFactory;
import org.slizaa.scanner.api.importer.IModelImporterFactory;
import org.slizaa.scanner.spi.parser.IParserFactory;
import org.slizaa.server.service.backend.IBackendService;
import org.slizaa.server.service.backend.IBackendServiceInstanceProvider;
import org.slizaa.server.service.backend.impl.dao.ISlizaaServerBackendDao;
import org.slizaa.server.service.extensions.IExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import com.google.common.io.ByteStreams;

/**
 * <p>
 * </p>
 */
@Component
public class SlizaaServerBackendImpl
		implements IBackendService, IBackendServiceInstanceProvider, IServerBackendStateMachineContext {

	/* the internal state machine */
	@Autowired
	private StateMachine<ServerBackendStateMachine.States, ServerBackendStateMachine.Events> _stateMachine;

	/* - */
	@Autowired
	private ISlizaaServerBackendDao _slizaaServerBackendDao;

	/* the dynamically loaded extensions */
	private DynamicallyLoadedExtensions _dynamicallyLoadedExtensions;

	@PostConstruct
	public void initialize() {
		_stateMachine.start();
	}

	@Override
	public ClassLoader getCurrentExtensionClassLoader() {
		return _dynamicallyLoadedExtensions != null ? _dynamicallyLoadedExtensions.getClassLoader() : null;
	}

	@Override
	public List<IExtension> getInstalledExtensions() {
		return _slizaaServerBackendDao.getInstalledExtensions();
	}

	@Override
	public void installExtensions(List<IExtension> extensionsToInstall) {

		checkNotNull(extensionsToInstall);

		_stateMachine.sendEvent(MessageBuilder
				.withPayload(ServerBackendStateMachine.Events.UPDATE_BACKEND_CONFIGURATION)
				.setHeader(ServerBackendStateMachine.HEADER_EXTENSIONS_TO_INSTALL, extensionsToInstall).build());
	}

	@Override
	public void uninstallAllExtensions() {
		
		_stateMachine.sendEvent(MessageBuilder
				.withPayload(ServerBackendStateMachine.Events.CLEAR_BACKEND_CONFIGURATION)
				.build());
	}
	
	@Override
	public boolean hasInstalledExtensions() {
		return _dynamicallyLoadedExtensions != null;
	}

	@Override
	public boolean hasModelImporterFactory() {
		checkState(hasInstalledExtensions(), "SlizaaServerBackend has no installed extensions.");
		return _dynamicallyLoadedExtensions.hasModelImporterFactory();
	}

	@Override
	public IModelImporterFactory getModelImporterFactory() {
		checkState(hasInstalledExtensions(), "SlizaaServerBackend has no installed extensions.");
		return _dynamicallyLoadedExtensions.getModelImporterFactory();
	}

	@Override
	public boolean hasGraphDbFactory() {
		checkState(hasInstalledExtensions(), "SlizaaServerBackend has no installed extensions.");
		return _dynamicallyLoadedExtensions.hasGraphDbFactory();
	}

	@Override
	public IGraphDbFactory getGraphDbFactory() {
		checkState(hasInstalledExtensions(), "SlizaaServerBackend has no installed extensions.");
		return _dynamicallyLoadedExtensions.getGraphDbFactory();
	}

	@Override
	public ICypherStatementRegistry getCypherStatementRegistry() {
		checkState(hasInstalledExtensions(), "SlizaaServerBackend has no installed extensions.");
		return _dynamicallyLoadedExtensions.getCypherStatementRegistry();
	}

	@Override
	public List<IParserFactory> getParserFactories() {
		checkState(hasInstalledExtensions(), "SlizaaServerBackend has no installed extensions.");
		return _dynamicallyLoadedExtensions.getParserFactories();
	}

	@Override
	public List<IMappingProvider> getMappingProviders() {
		checkState(hasInstalledExtensions(), "SlizaaServerBackend has no installed extensions.");
		return _dynamicallyLoadedExtensions.getMappingProviders();
	}

	@Override
	public boolean canConfigureBackend() {
		return !getInstalledExtensions().isEmpty();
	}

	@Override
	public boolean configureBackend() {

		try {
			DynamicallyLoadedExtensions newDynamicallyLoadedExtensions = new DynamicallyLoadedExtensions(
					dynamicallyLoadExtensions(_slizaaServerBackendDao.getInstalledExtensions()));

			this._dynamicallyLoadedExtensions = newDynamicallyLoadedExtensions;
			this._dynamicallyLoadedExtensions.initialize();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void unconfigureBackend() {
		if (this._dynamicallyLoadedExtensions != null) {
			this._dynamicallyLoadedExtensions.dispose();
			this._dynamicallyLoadedExtensions = null;
		}
	}

	@Override
	public boolean updateBackendConfiguration(List<IExtension> extensionsToInstall) {

		try {

			DynamicallyLoadedExtensions newDynamicallyLoadedExtensions = new DynamicallyLoadedExtensions(
					dynamicallyLoadExtensions(extensionsToInstall));

			this._dynamicallyLoadedExtensions = newDynamicallyLoadedExtensions;
			this._dynamicallyLoadedExtensions.initialize();

			_slizaaServerBackendDao.saveInstalledExtensions(extensionsToInstall);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public byte[] loadResourceFromExtensions(String path) {

		if (hasInstalledExtensions()) {

			ClassPathResource imgFile = new ClassPathResource(path, getCurrentExtensionClassLoader());

			if (imgFile.exists()) {
				try {
					return ByteStreams.toByteArray(imgFile.getInputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @return
	 */
	private static ClassLoader dynamicallyLoadExtensions(List<IExtension> extensionToDynamicallyLoad) {

		List<URL> urlList = checkNotNull(extensionToDynamicallyLoad).stream()
				.flatMap(ext -> ext.resolvedArtifactsToInstall().stream()).distinct().collect(Collectors.toList());

		return new URLClassLoader(urlList.toArray(new URL[0]), SlizaaServerBackendImpl.class.getClassLoader());
	}
}

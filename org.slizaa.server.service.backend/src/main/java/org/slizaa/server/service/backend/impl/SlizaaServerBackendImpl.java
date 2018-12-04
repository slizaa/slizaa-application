package org.slizaa.server.service.backend.impl;

import org.slizaa.core.mvnresolver.MvnResolverServiceFactoryFactory;
import org.slizaa.core.mvnresolver.api.IMvnResolverService;
import org.slizaa.core.mvnresolver.api.IMvnResolverServiceFactory;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IMappingProvider;
import org.slizaa.scanner.api.cypherregistry.ICypherStatementRegistry;
import org.slizaa.scanner.api.graphdb.IGraphDbFactory;
import org.slizaa.scanner.api.importer.IModelImporterFactory;
import org.slizaa.scanner.spi.parser.IParserFactory;
import org.slizaa.server.service.backend.ISlizaaServerBackend;
import org.slizaa.server.service.backend.dao.ISlizaaServerBackendDao;
import org.slizaa.server.service.extensions.IExtension;
import org.slizaa.server.service.extensions.IExtensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkState;

/**
 * <p>
 * </p>
 */
@Component
public class SlizaaServerBackendImpl implements ISlizaaServerBackend,
        IServerBackendStateMachineContext {

    /* the internal state machine */
    @Autowired
    private StateMachine<ServerBackendStateMachine.States, ServerBackendStateMachine.Events> _stateMachine;

    /* - */
    @Autowired
    private IExtensionService _extensionService;

    /* - */
    @Autowired
    private ISlizaaServerBackendDao _slizaaServerBackendDao;

    /* the dynamically loaded extensions */
    private DynamicallyLoadedExtensions _dynamicallyLoadedExtensions;

    /**
     *
     */
    @PostConstruct
    public void initialize() {

        // TODO: Remove
        _slizaaServerBackendDao.saveInstalledExtensions(_extensionService.getExtensions());

        //
        _stateMachine.start();
    }

    /**
     * @return
     */
    @Override
    public ClassLoader getCurrentExtensionClassLoader() {
        return _dynamicallyLoadedExtensions != null ? _dynamicallyLoadedExtensions.getClassLoader() : null;
    }

    /**
     * @return
     */
    @Override
    public List<IExtension> getInstalledExtensions() {
        return _slizaaServerBackendDao.getInstalledExtensions();
    }

    /**
     * @param extensions
     */
    @Override
    public void installExtensions(List<IExtension> extensions) {

        // TODO
        _stateMachine.sendEvent(MessageBuilder
                .withPayload(ServerBackendStateMachine.Events.UPDATE_BACKEND_CONFIGURATION)
                .build());
    }

    @Override
    public boolean isConfigured() {
        return _dynamicallyLoadedExtensions != null;
    }

    @Override
    public boolean hasModelImporterFactory() {
        checkState(isConfigured(), "ISlizaaServerBackend has to be configured.");
        return _dynamicallyLoadedExtensions.hasModelImporterFactory();
    }

    @Override
    public IModelImporterFactory getModelImporterFactory() {
        checkState(isConfigured(), "ISlizaaServerBackend has to be configured.");
        return _dynamicallyLoadedExtensions.getModelImporterFactory();
    }

    @Override
    public boolean hasGraphDbFactory() {
        checkState(isConfigured(), "ISlizaaServerBackend has to be configured.");
        return _dynamicallyLoadedExtensions.hasGraphDbFactory();
    }

    @Override
    public IGraphDbFactory getGraphDbFactory() {
        checkState(isConfigured(), "ISlizaaServerBackend has to be configured.");
        return _dynamicallyLoadedExtensions.getGraphDbFactory();
    }

    @Override
    public ICypherStatementRegistry getCypherStatementRegistry() {
        checkState(isConfigured(), "ISlizaaServerBackend has to be configured.");
        return _dynamicallyLoadedExtensions.getCypherStatementRegistry();
    }

    @Override
    public List<IParserFactory> getParserFactories() {
        checkState(isConfigured(), "ISlizaaServerBackend has to be configured.");
        return _dynamicallyLoadedExtensions.getParserFactories();
    }

    @Override
    public List<IMappingProvider> getMappingProviders() {
        checkState(isConfigured(), "ISlizaaServerBackend has to be configured.");
        return _dynamicallyLoadedExtensions.getMappingProviders();
    }

    @Override
    public boolean canConfigureBackend() {
        return !getInstalledExtensions().isEmpty();
    }

    @Override
    public void configureBackend() {
        this._dynamicallyLoadedExtensions = new DynamicallyLoadedExtensions(dynamicallyLoadExtensions());
        this._dynamicallyLoadedExtensions.initialize();
    }

    @Override
    public void unconfigureBackend() {
        if (this._dynamicallyLoadedExtensions != null) {
            this._dynamicallyLoadedExtensions.dispose();
            this._dynamicallyLoadedExtensions = null;
        }
    }

    @Override
    public boolean updateBackendConfiguration() {
        try {
            dynamicallyLoadExtensions();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    private ClassLoader dynamicallyLoadExtensions() {

        //
        IMvnResolverServiceFactory resolverServiceFactory = MvnResolverServiceFactoryFactory
                .createNewResolverServiceFactory();

        //
        IMvnResolverService mvnResolverService = resolverServiceFactory.newMvnResolverService().create();

        //
        List<URL> urlList = _extensionService.getExtensions().stream()
                .flatMap(ext -> ext.resolvedArtifactsToInstall().stream()).distinct()
                .collect(Collectors.toList());

        //
        return new URLClassLoader(urlList.toArray(new URL[0]), SlizaaServerBackendImpl.class.getClassLoader());
    }
}

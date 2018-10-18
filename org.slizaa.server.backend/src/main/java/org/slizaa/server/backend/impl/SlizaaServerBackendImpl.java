package org.slizaa.server.backend.impl;

import org.slizaa.core.mvnresolver.MvnResolverServiceFactoryFactory;
import org.slizaa.core.mvnresolver.api.IMvnResolverService;
import org.slizaa.core.mvnresolver.api.IMvnResolverServiceFactory;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IMappingProvider;
import org.slizaa.scanner.api.cypherregistry.ICypherStatementRegistry;
import org.slizaa.scanner.api.graphdb.IGraphDbFactory;
import org.slizaa.scanner.api.importer.IModelImporterFactory;
import org.slizaa.scanner.spi.parser.IParserFactory;
import org.slizaa.server.backend.ISlizaaServerBackend;
import org.slizaa.server.backend.dao.ISlizaaServerDao;
import org.slizaa.server.backend.extensions.IExtension;
import org.slizaa.server.backend.extensions.IExtensionService;
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
 *
 */
@Component
public class SlizaaServerBackendImpl implements ISlizaaServerBackend,
    ServerBackendStateMachine.IServerBackendStateMachineContext {

  /* the internal state machine */
  @Autowired
  private StateMachine<ServerBackendStateMachine.States, ServerBackendStateMachine.Events> _stateMachine;

  /* - */
  @Autowired
  private IExtensionService _extensionService;

  /* - */
  @Autowired
  private ISlizaaServerDao _slizaaServerDao;

  /*  */
  private ConfiguredBackend _configuredBackend;

  /**
   *
   */
  @PostConstruct
  public void initialize() {

    // TODO: Remove
    _slizaaServerDao.saveInstalledExtensions(_extensionService.getAvailableExtensions());

    _stateMachine.start();
  }

  /**
   * @return
   */
  @Override
  public ClassLoader getCurrentExtensionClassLoader() {
    return _configuredBackend != null ? _configuredBackend.getClassLoader() : null;
  }

  /**
   * @return
   */
  @Override
  public List<IExtension> getInstalledExtensions() {
    return _slizaaServerDao.getInstalledExtensions();
  }

  /**
   * @param extensions
   */
  @Override
  public void installExtensions(IExtension... extensions) {

    // TODO
    _stateMachine.sendEvent(MessageBuilder
        .withPayload(ServerBackendStateMachine.Events.UPDATE_BACKEND_CONFIGURATION)
        .build());
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
    return _configuredBackend != null;
  }

  @Override
  public boolean hasModelImporterFactory() {
    checkState(isConfigured(), "Slizaa server backend has to be configured.");
    return _configuredBackend.hasModelImporterFactory();
  }

  @Override
  public IModelImporterFactory getModelImporterFactory() {
    checkState(isConfigured(), "Slizaa server backend has to be configured.");
    return _configuredBackend.getModelImporterFactory();
  }

  @Override
  public boolean hasGraphDbFactory() {
    checkState(isConfigured(), "Slizaa server backend has to be configured.");
    return _configuredBackend.hasGraphDbFactory();
  }

  @Override
  public IGraphDbFactory getGraphDbFactory() {
    checkState(isConfigured(), "Slizaa server backend has to be configured.");
    return _configuredBackend.getGraphDbFactory();
  }

  @Override
  public ICypherStatementRegistry getCypherStatementRegistry() {
    checkState(isConfigured(), "Slizaa server backend has to be configured.");
    return _configuredBackend.getCypherStatementRegistry();
  }

  @Override
  public List<IParserFactory> getParserFactories() {
    checkState(isConfigured(), "Slizaa server backend has to be configured.");
    return _configuredBackend.getParserFactories();
  }

  @Override
  public List<IMappingProvider> getMappingProviders() {
    checkState(isConfigured(), "Slizaa server backend has to be configured.");
    return _configuredBackend.getMappingProviders();
  }

  @Override
  public boolean canConfigureBackend() {
    return !getInstalledExtensions().isEmpty();
  }

  @Override
  public void configureBackend() {
    this._configuredBackend = new ConfiguredBackend(dynamicallyLoadExtensions());
    this._configuredBackend.initialize();
  }

  @Override
  public void unconfigureBackend() {
    if (this._configuredBackend == null) {
      this._configuredBackend.dispose();
      this._configuredBackend = null;
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
    List<URL> urlList = _extensionService.getAvailableExtensions().stream()
        .flatMap(ext -> ext.resolvedArtifactsToInstall().stream()).distinct()
        .collect(Collectors.toList());

    //
    return new URLClassLoader(urlList.toArray(new URL[0]), SlizaaServerBackendImpl.class.getClassLoader());
  }
}

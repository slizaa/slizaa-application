package org.slizaa.server.backend.impl;

import org.slizaa.core.mvnresolver.MvnResolverServiceFactoryFactory;
import org.slizaa.core.mvnresolver.api.IMvnResolverService;
import org.slizaa.core.mvnresolver.api.IMvnResolverServiceFactory;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IMappingProvider;
import org.slizaa.scanner.api.cypherregistry.ICypherStatementRegistry;
import org.slizaa.scanner.api.graphdb.IGraphDbFactory;
import org.slizaa.scanner.api.importer.IModelImporterFactory;
import org.slizaa.scanner.spi.parser.IParserFactory;
import org.slizaa.server.backend.IExtension;
import org.slizaa.server.backend.ISlizaaServerBackend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;

/**
 *
 */
@Component
public class SlizaaServerBackendImpl implements ISlizaaServerBackend,
    ServerBackendStateMachine.IServerBackendStateMachineContext {

  /**
   * -
   */
  @Autowired
  private StateMachine<ServerBackendStateMachine.States, ServerBackendStateMachine.Events> _stateMachine;

  /**
   * -
   */
  private ConfiguredBackend _configuredBackend;

  /**
   * -
   */
  private MavenBasedExtension defaultExtension = new MavenBasedExtension()
      .withDependency("org.slizaa.neo4j:org.slizaa.neo4j.importer:1.0.0-SNAPSHOT")
      .withDependency("org.slizaa.neo4j:org.slizaa.neo4j.graphdbfactory:1.0.0-SNAPSHOT")
      .withDependency("org.slizaa.jtype:org.slizaa.jtype.scanner:1.0.0-SNAPSHOT")
      .withDependency("org.slizaa.jtype:org.slizaa.jtype.scanner.apoc:1.0.0-SNAPSHOT")
      .withDependency("org.slizaa.jtype:org.slizaa.jtype.hierarchicalgraph:1.0.0-SNAPSHOT")
      .withExclusionPattern("*:org.slizaa.scanner.spi-api").withExclusionPattern("*:jdk.tools");

  // TODO: replace with dao
  private List<IExtension> _extensionList = Collections.singletonList(defaultExtension);

  /**
   *
   */
  @PostConstruct
  public void initialize() {
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
    return _extensionList;
  }

  /**
   * @return
   */
  @Override
  public List<IExtension> getAvailableExtensions() {
    return _extensionList;
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
    this._configuredBackend.dispose();
    this._configuredBackend = null;
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
    URL[] resolvedArtifacts = this.defaultExtension.resolvedArtifactsToInstall();

    //
    return new URLClassLoader(resolvedArtifacts, SlizaaServerBackendImpl.class.getClassLoader());
  }
}

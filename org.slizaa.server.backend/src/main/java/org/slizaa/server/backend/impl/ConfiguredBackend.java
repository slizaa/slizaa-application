package org.slizaa.server.backend.impl;

import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IMappingProvider;
import org.slizaa.scanner.api.cypherregistry.ICypherStatement;
import org.slizaa.scanner.api.cypherregistry.ICypherStatementRegistry;
import org.slizaa.scanner.api.graphdb.IGraphDbFactory;
import org.slizaa.scanner.api.importer.IModelImporterFactory;
import org.slizaa.scanner.cypherregistry.CypherRegistryUtils;
import org.slizaa.scanner.cypherregistry.CypherStatementRegistry;
import org.slizaa.scanner.spi.parser.IParserFactory;
import org.slizaa.server.backend.IExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public class ConfiguredBackend {

  //
  private IModelImporterFactory _modelImporterFactory;

  //
  private IGraphDbFactory _graphDbFactory;

  //
  private List<IParserFactory> _parserFactories;

  //
  private List<IMappingProvider> _mappingProviders;

  //
  private ICypherStatementRegistry _cypherStatementRegistry;

  //
  private List<ICypherStatement> _cypherStatements = new ArrayList<>();

  /** - */
  private ClassLoader _classLoader;

  /**
   *
   * @param classLoader
   */
  public ConfiguredBackend(ClassLoader classLoader) {
    this._classLoader = checkNotNull(classLoader);
  }

  public void initialize() {
    _cypherStatements = new ArrayList<>();
    this._cypherStatementRegistry = new CypherStatementRegistry(() -> {
      return Collections.unmodifiableList(_cypherStatements);
    });

    this.configure();
  }

  public void dispose() {
    _cypherStatements = null;
    _modelImporterFactory = null;
    _graphDbFactory = null;
    _parserFactories = null;
    _mappingProviders = null;
    _cypherStatements = null;
    _classLoader = null;
  }

  public ClassLoader getClassLoader() {
    return _classLoader;
  }

  public ICypherStatementRegistry getCypherStatementRegistry() {
    return _cypherStatementRegistry;
  }

  public boolean hasModelImporterFactory() {
    return _modelImporterFactory != null;
  }

  public IModelImporterFactory getModelImporterFactory() {
    return _modelImporterFactory;
  }

  public boolean hasGraphDbFactory() {
    return _graphDbFactory != null;
  }

  public IGraphDbFactory getGraphDbFactory() {
    return _graphDbFactory;
  }

  public List<IParserFactory> getParserFactories() {
    return _parserFactories;
  }

  public List<IMappingProvider> getMappingProviders() {
    return _mappingProviders;
  }

  /**
   * @return
   */
  private boolean configure() {

    //
    ServiceLoader<IModelImporterFactory> serviceLoader = ServiceLoader.load(IModelImporterFactory.class,
        _classLoader);
    this._modelImporterFactory = serviceLoader.iterator().next();

    //
    ServiceLoader<IGraphDbFactory> serviceLoader_2 = ServiceLoader.load(IGraphDbFactory.class,
        _classLoader);
    this._graphDbFactory = serviceLoader_2.iterator().next();

    //
    ServiceLoader<IParserFactory> serviceLoader_3 = ServiceLoader.load(IParserFactory.class,
        _classLoader);
    this._parserFactories = new ArrayList<>();
    serviceLoader_3.iterator().forEachRemaining(mp -> this._parserFactories.add(mp));

    //
    ServiceLoader<IMappingProvider> serviceLoader_4 = ServiceLoader.load(IMappingProvider.class,
        _classLoader);
    this._mappingProviders = new ArrayList<>();
    serviceLoader_4.iterator().forEachRemaining(mp -> this._mappingProviders.add(mp));

    //
    _cypherStatements.clear();
    _cypherStatements.addAll(CypherRegistryUtils.getCypherStatementsFromClasspath(_classLoader));
    this._cypherStatementRegistry = new CypherStatementRegistry(() -> {
      return Collections.unmodifiableList(_cypherStatements);
    });
    this._cypherStatementRegistry.rescan();

    //
    return true;
  }
}

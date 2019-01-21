package org.slizaa.server.service.backend.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IMappingProvider;
import org.slizaa.scanner.api.cypherregistry.ICypherStatement;
import org.slizaa.scanner.api.cypherregistry.ICypherStatementRegistry;
import org.slizaa.scanner.api.graphdb.IGraphDbFactory;
import org.slizaa.scanner.api.importer.IModelImporterFactory;
import org.slizaa.scanner.cypherregistry.CypherRegistryUtils;
import org.slizaa.scanner.cypherregistry.CypherStatementRegistry;
import org.slizaa.scanner.spi.parser.IParserFactory;

/**
 *
 */
public class DynamicallyLoadedExtensions {

  private IModelImporterFactory _modelImporterFactory;

  private IGraphDbFactory _graphDbFactory;

  private List<IParserFactory> _parserFactories;

  private List<IMappingProvider> _mappingProviders;

  private ICypherStatementRegistry _cypherStatementRegistry;

  private List<ICypherStatement> _cypherStatements = new ArrayList<>();

  private ClassLoader _classLoader;

  /**
   *
   * @param classLoader
   */
  public DynamicallyLoadedExtensions(ClassLoader classLoader) {
    this._classLoader = checkNotNull(classLoader);
  }

  /**
   *
   */
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
  private void configure() {

    // load the IModelImporterFactory instance
    this._modelImporterFactory = loadSingleInstance(IModelImporterFactory.class);

    // load the IGraphDbFactory instance
    this._graphDbFactory = loadSingleInstance(IGraphDbFactory.class);

    // load all IParserFactory instances
    this._parserFactories = loadAllInstances(IParserFactory.class);

    // load all IMappingProvider instances
    this._mappingProviders  = loadAllInstances(IMappingProvider.class);

    // reload the cypher registry
    _cypherStatements.clear();
    _cypherStatements.addAll(CypherRegistryUtils.getCypherStatementsFromClasspath(_classLoader));
    this._cypherStatementRegistry = new CypherStatementRegistry(() -> {
      return Collections.unmodifiableList(_cypherStatements);
    });
    this._cypherStatementRegistry.rescan();
  }

  /**
   *
   * @param <T>
   * @return
   */
  private <T> T loadSingleInstance(Class<T> type) {
    ServiceLoader<T> serviceLoader = ServiceLoader.load(type, _classLoader);
    Iterator<T> iterator = serviceLoader.iterator();
    return iterator.hasNext() ? iterator.next() : null;
  }

  /**
   *
   * @param type
   * @param <T>
   * @return
   */
  private <T> List<T> loadAllInstances(Class<T> type) {
    ServiceLoader<T> serviceLoader = ServiceLoader.load(type, _classLoader);
    Iterator<T> iterator = serviceLoader.iterator();
    List<T> result = new ArrayList<>();
    iterator.forEachRemaining(entry -> result.add(entry));
    return result;
  }
}

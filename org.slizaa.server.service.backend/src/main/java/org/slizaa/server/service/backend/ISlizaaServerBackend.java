package org.slizaa.server.service.backend;

import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IMappingProvider;
import org.slizaa.scanner.api.cypherregistry.ICypherStatementRegistry;
import org.slizaa.scanner.api.graphdb.IGraphDbFactory;
import org.slizaa.scanner.api.importer.IModelImporterFactory;
import org.slizaa.scanner.spi.parser.IParserFactory;
import org.slizaa.server.service.extensions.IExtension;

import java.util.List;

/**
 *
 */
public interface ISlizaaServerBackend {

  /**
   *
   * @return
   */
  ClassLoader getCurrentExtensionClassLoader();

  /**
   *
   * @return
   */
  List<IExtension> getInstalledExtensions();

  /**
   *
   * @param extensions
   */
  void installExtensions(IExtension... extensions);

  /**
   *
   * @param extensions
   */
  void installExtensions(List<IExtension> extensions);

  /**
   *
   * @return
   */
  boolean isConfigured();

  /**
   *
   * @return
   */
  boolean hasModelImporterFactory();

  /**
   *
   * @return
   */
  IModelImporterFactory getModelImporterFactory();

  /**
   *
   * @return
   */
  boolean hasGraphDbFactory();

  /**
   *
   * @return
   */
  IGraphDbFactory getGraphDbFactory();

  /**
   *
   * @return
   */
  ICypherStatementRegistry getCypherStatementRegistry();

  /**
   *
   * @return
   */
  List<IParserFactory> getParserFactories();

  /**
   *
   * @return
   */
  List<IMappingProvider> getMappingProviders();
}

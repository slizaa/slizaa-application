package org.slizaa.server.backend;

import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IMappingProvider;
import org.slizaa.scanner.api.cypherregistry.ICypherStatementRegistry;
import org.slizaa.scanner.api.graphdb.IGraphDbFactory;
import org.slizaa.scanner.api.importer.IModelImporterFactory;
import org.slizaa.scanner.spi.parser.IParserFactory;
import org.slizaa.server.backend.extensions.IExtension;

import java.util.List;

/**
 *
 */
public interface ISlizaaServerBackend {

  List<IExtension> getInstalledExtensions();

  void installExtensions(IExtension... extensions);

  void installExtensions(List<IExtension> extensions);

  ClassLoader getCurrentExtensionClassLoader();

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

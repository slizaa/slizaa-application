package org.slizaa.server.service.backend;

import java.util.List;

import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IMappingProvider;
import org.slizaa.scanner.api.cypherregistry.ICypherStatementRegistry;
import org.slizaa.scanner.api.graphdb.IGraphDbFactory;
import org.slizaa.scanner.api.importer.IModelImporterFactory;
import org.slizaa.scanner.spi.parser.IParserFactory;

public interface IBackendServiceInstanceProvider extends IBackendService {

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

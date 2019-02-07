package org.slizaa.server.service.slizaa;

import java.util.List;

import org.slizaa.server.service.backend.IBackendService;
import org.slizaa.server.service.extensions.IExtensionService;
import org.slizaa.server.service.svg.ISvgService;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface ISlizaaService {

	IExtensionService getExtensionService();

	IBackendService getBackendService();
	
	ISvgService getSvgService();

	boolean hasStructureDatabases();

	List<? extends IGraphDatabase> getGraphDatabases();

	IGraphDatabase getGraphDatabase(String identifier);
	
	IGraphDatabase newGraphDatabase(String identifier);
}

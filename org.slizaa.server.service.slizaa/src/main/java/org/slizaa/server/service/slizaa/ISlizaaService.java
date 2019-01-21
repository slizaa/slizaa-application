package org.slizaa.server.service.slizaa;

import java.util.List;

import org.slizaa.server.service.backend.IBackendService;
import org.slizaa.server.service.extensions.IExtensionService;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface ISlizaaService {

	IExtensionService getExtensionService();

	IBackendService getBackendService();

	boolean hasStructureDatabases();

	List<? extends IStructureDatabase> getStructureDatabases();

	IStructureDatabase getStructureDatabase(String identifier);
	
	IStructureDatabase newStructureDatabase(String identifier);
}

package org.slizaa.server.service.backend;

import java.util.List;

import org.slizaa.server.service.extensions.IExtension;

/**
 *
 */
public interface IBackendService {

	/**
	 *
	 * @return
	 */
	boolean hasInstalledExtensions();

	/**
	 *
	 * @return
	 */
	List<IExtension> getInstalledExtensions();

	/**
	 *
	 * @param extensions
	 */
	void installExtensions(List<IExtension> extensions);
	
	/**
	 * 
	 */
	void uninstallAllExtensions();

	/**
	 *
	 * @return
	 */
	ClassLoader getCurrentExtensionClassLoader();

	/**
	 * 
	 * @param path
	 * @return
	 */
	byte[] loadResourceFromExtensions(String path);
}

package org.slizaa.server.service.slizaa;

import org.slizaa.server.service.extensions.IExtension;
import org.slizaa.server.service.extensions.IExtensionService;
import org.slizaa.server.service.extensions.IExtensionIdentifier;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface ISlizaaService {

  /**
   *
   * @return
   */
  boolean isBackendConfigured();

  /**
   *
   * @return
   */
  List<IExtension> getInstalledExtensions();

  /**
   *
   * @param extensionIdentifiers
   * @return
   */
  List<IExtension> installExtensions(List<? extends IExtensionIdentifier> extensionIdentifiers);

  /**
   *
   * @param extensionIds
   * @return
   */
  List<IExtension> uninstallExtensions(List<? extends IExtensionIdentifier> extensionIds);

  /**
   *
   * @return
   */
  IExtensionService getExtensionService();

  /**
   *
   * @return
   */
  boolean hasStructureDatabases();

  /**
   *
   * @return
   */
  List<? extends IStructureDatabase> getStructureDatabases();

  /**
   *
   * @param identifier
   * @return
   */
  IStructureDatabase newStructureDatabase(String identifier);
}

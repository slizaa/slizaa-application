package org.slizaa.server.service.extensions;

import java.util.List;

/**
 *
 */
public interface IExtensionService {

  /**
   *
   * @return
   */
  List<IExtension> getExtensions();

  /**
   *
   * @return
   */
  List<IExtension> getExtensions(List<IExtensionIdentifier> extensionIdentifiers);
}

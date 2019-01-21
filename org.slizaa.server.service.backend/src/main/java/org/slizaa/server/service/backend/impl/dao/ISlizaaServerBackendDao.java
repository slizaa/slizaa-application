package org.slizaa.server.service.backend.impl.dao;

import org.slizaa.server.service.extensions.IExtension;

import java.util.List;

/**
 *
 */
public interface ISlizaaServerBackendDao {

  /**
   *
   * @return
   */
  List<IExtension> getInstalledExtensions();

  /**
   *
   * @param extensions
   */
  void saveInstalledExtensions(List<IExtension> extensions);
}

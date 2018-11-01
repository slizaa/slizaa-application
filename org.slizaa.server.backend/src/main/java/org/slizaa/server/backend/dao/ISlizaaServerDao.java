package org.slizaa.server.backend.dao;

import org.slizaa.server.extensions.IExtension;

import java.util.List;

/**
 *
 */
public interface ISlizaaServerDao {

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

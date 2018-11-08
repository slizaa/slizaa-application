package org.slizaa.server.service.backend.dao;

import org.slizaa.server.service.extensions.IExtension;

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
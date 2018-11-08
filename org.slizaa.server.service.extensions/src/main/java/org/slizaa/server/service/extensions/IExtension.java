package org.slizaa.server.service.extensions;

import java.net.URL;
import java.util.List;

/**
 *
 */
public interface IExtension {

  /**
   * The identifier of this extension.
   *
   * @return
   */
  String getIdentifier();

  /**
   * The version of this extension.
   *
   * @return
   */
  Version getVersion();

  /**
   * Returns a list of artifact URLs to install for this extension.
   *
   * @return
   */
  List<URL> resolvedArtifactsToInstall();
}

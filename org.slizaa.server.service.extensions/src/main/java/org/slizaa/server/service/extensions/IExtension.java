package org.slizaa.server.service.extensions;

import java.net.URL;
import java.util.List;

/**
 *
 */
public interface IExtension extends IExtensionIdentifier {

  /**
   * Returns a list of artifact URLs to install for this extension.
   *
   * @return
   */
  List<URL> resolvedArtifactsToInstall();
}

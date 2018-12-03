package org.slizaa.server.service.extensions;

import java.net.URL;
import java.util.List;

/**
 *
 */
public interface IExtensionIdentifier {

  /**
   * The symbolic name of this extension.
   *
   * @return
   */
  String getSymbolicName();

  /**
   * The version of this extension.
   *
   * @return
   */
  Version getVersion();
}

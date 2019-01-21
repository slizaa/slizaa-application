package org.slizaa.server.service.extensions;

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

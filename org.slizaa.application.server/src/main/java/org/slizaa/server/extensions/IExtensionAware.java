package org.slizaa.server.extensions;

import java.util.List;

public interface IExtensionAware {

  List<IExtension> getInstalledExtensions();

  List<IExtension> getAvailableExtensions();

  void installExtensions(IExtension... extensions);

  void installExtensions(List<IExtension> extensions);
}

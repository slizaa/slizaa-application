package org.slizaa.server.backend.dao;

import org.slizaa.server.backend.extensions.IExtension;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class DefaultSlizaaServerDao implements ISlizaaServerDao {

  /* - */
  private List<IExtension> _extensions;

  /**
   * @return
   */
  @Override
  public List<IExtension> getInstalledExtensions() {
    return _extensions == null ? Collections.emptyList() : Collections.unmodifiableList(_extensions);
  }

  /**
   * @param extensions
   */
  @Override
  public void saveInstalledExtensions(List<IExtension> extensions) {
    _extensions = extensions;
  }
}

package org.slizaa.server.service.backend;

import org.junit.runner.RunWith;
import org.slizaa.server.service.extensions.IExtension;
import org.slizaa.server.service.extensions.IExtensionIdentifier;
import org.slizaa.server.service.extensions.IExtensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AbstractServerBackendTest.class)
@TestConfiguration
@ComponentScan(basePackageClasses = AbstractServerBackendTest.class)
public abstract class AbstractServerBackendTest {

  @Autowired
  protected ApplicationContext applicationContext;

  @Autowired
  protected DummyExtensionService extensionService;

  /**
   *
   */
  @Component
  public static class DummyExtensionService implements IExtensionService {

    //
    private List<IExtension> _extensions = Collections.emptyList();

    /**
     *
     * @return
     */
    @Override
    public List<IExtension> getExtensions() {
      return _extensions;
    }

    /**
     *
     * @param extensionIdentifiers
     * @return
     */
    @Override
    public List<IExtension> getExtensions(List<? extends IExtensionIdentifier> extensionIdentifiers) {
      return getExtensions().stream().filter(extensionIdentifiers::contains).collect(Collectors.toList());
    }

    /**
     *
     * @param _extensions
     */
    public void setExtensions(List<IExtension> _extensions) {
      this._extensions = _extensions;
    }
  }
}


package org.slizaa.server.backend.impl;

import org.slizaa.core.mvnresolver.MvnResolverServiceFactoryFactory;
import org.slizaa.core.mvnresolver.api.IMvnResolverService;
import org.slizaa.core.mvnresolver.api.IMvnResolverServiceFactory;
import org.slizaa.server.backend.IExtension;
import org.slizaa.server.backend.ISlizaaServerBackend;
import org.slizaa.server.backend.ISlizaaServerBackendListener;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public abstract class AbstractExtensionServiceImpl implements ISlizaaServerBackend {

  /** - */
  private MavenBasedExtension defaultExtension = new MavenBasedExtension()
      .withDependency("org.slizaa.neo4j:org.slizaa.neo4j.importer:1.0.0-SNAPSHOT")
      .withDependency("org.slizaa.neo4j:org.slizaa.neo4j.graphdbfactory:1.0.0-SNAPSHOT")
      .withDependency("org.slizaa.jtype:org.slizaa.jtype.scanner:1.0.0-SNAPSHOT")
      .withDependency("org.slizaa.jtype:org.slizaa.jtype.scanner.apoc:1.0.0-SNAPSHOT")
      .withDependency("org.slizaa.jtype:org.slizaa.jtype.hierarchicalgraph:1.0.0-SNAPSHOT")
      .withExclusionPattern("*:org.slizaa.scanner.spi-api").withExclusionPattern("*:jdk.tools");

  /** - */
  private List<IExtension> extensionList = Collections.singletonList(defaultExtension);

  /** - */
  private ClassLoader _classLoader;

  /** - */
  private CopyOnWriteArrayList<ISlizaaServerBackendListener> _listenerList;

  /**
   * Creates a new instance of type {@link AbstractExtensionServiceImpl}.
   */
  public AbstractExtensionServiceImpl() {
    _listenerList = new CopyOnWriteArrayList<>();
  }

  /**
   *
   * @return
   */
  @Override
  public ClassLoader getCurrentExtensionClassLoader() {

    //
    dynamicallyLoadExtensions();

    //
    return _classLoader;
  }

  @Override
  public List<IExtension> getInstalledExtensions() {
    return extensionList;
  }

  @Override
  public List<IExtension> getAvailableExtensions() {
    return extensionList;
  }

  @Override
  public void installExtensions(IExtension... extensions) {
    _classLoader = null;
  }

  @Override
  public void installExtensions(List<IExtension> extensions) {
    _classLoader = null;
  }

  /**
   *
   * @param listener
   */
  @Override
  public void removeSlizaaServerBackendListener(ISlizaaServerBackendListener listener) {
    _listenerList.add(checkNotNull(listener));
  }

  /**
   *
   * @param listener
   */
  @Override
  public void addSlizaaServerBackendListener(ISlizaaServerBackendListener listener) {
    _listenerList.remove(checkNotNull(listener));
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  private void dynamicallyLoadExtensions() {

    //
    if (_classLoader != null) {
      return;
    }

    //
    IMvnResolverServiceFactory resolverServiceFactory = MvnResolverServiceFactoryFactory
        .createNewResolverServiceFactory();

    //
    IMvnResolverService mvnResolverService = resolverServiceFactory.newMvnResolverService().create();

    //
    URL[] resolvedArtifacts = this.defaultExtension.resolvedArtifactsToInstall();

    //
    _classLoader = new URLClassLoader(resolvedArtifacts, AbstractExtensionServiceImpl.class.getClassLoader());

    //
    fireExtensionsChanged();
  }

  /**
   *
   */
  private void fireExtensionsChanged() {

    //
    _listenerList.forEach(l -> {
      try {
        l.extensionsChanged();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
}

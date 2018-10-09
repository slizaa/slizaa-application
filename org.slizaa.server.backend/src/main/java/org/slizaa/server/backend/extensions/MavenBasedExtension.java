package org.slizaa.server.backend.extensions;

import org.slizaa.core.mvnresolver.MvnResolverServiceFactoryFactory;
import org.slizaa.core.mvnresolver.api.IMvnResolverService;
import org.slizaa.core.mvnresolver.api.IMvnResolverServiceFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public class MavenBasedExtension implements IExtension {

  /* - */
  private String _identifier;

  /* - */
  private Version _version;

  /* - */
  private List<String> _dependencies;

  /* - */
  private List<String> _exclusionPatterns;

  /* - */
  private List<String> _inclusionPatterns;

  /**
   * Creates a new instance of type {@link MavenBasedExtension}.
   */
  public MavenBasedExtension(String identifier, Version version) {

    this._identifier = checkNotNull(identifier);
    this._version = checkNotNull(version);

    _dependencies = new ArrayList<>();
    _exclusionPatterns = new ArrayList<>();
    _inclusionPatterns = new ArrayList<>();
  }

  /**
   *
   * @return
   */
  @Override
  public String getIdentifier() {
    return this._identifier;
  }

  /**
   *
   * @return
   */
  @Override
  public Version getVersion() {
    return this._version;
  }

  /**
   *
   * @param mvnCoordinates
   * @return
   */
  public MavenBasedExtension withDependency(String mvnCoordinates) {
    _dependencies.add(checkNotNull(mvnCoordinates));
    return this;
  }

  /**
   *
   * @param mvnCoordinates
   * @return
   */
  public MavenBasedExtension withExclusionPattern(String mvnCoordinates) {
    _exclusionPatterns.add(checkNotNull(mvnCoordinates));
    return this;
  }

  /**
   *
   * @param mvnCoordinates
   * @return
   */
  public MavenBasedExtension withInclusionPatterns(String mvnCoordinates) {
    _inclusionPatterns.add(checkNotNull(mvnCoordinates));
    return this;
  }

  /**
   *
   * @return
   */
  @Override
  public List<URL> resolvedArtifactsToInstall() {

    //
    IMvnResolverServiceFactory resolverServiceFactory = MvnResolverServiceFactoryFactory
        .createNewResolverServiceFactory();

    //
    IMvnResolverService mvnResolverService = resolverServiceFactory.newMvnResolverService().create();
    IMvnResolverService.IMvnResolverJob resolverJob = mvnResolverService.newMvnResolverJob();
    _dependencies.forEach(dep -> resolverJob.withDependency(dep));
    _exclusionPatterns.forEach(p -> resolverJob.withExclusionPattern(p));
    _inclusionPatterns.forEach(p -> resolverJob.withInclusionPattern(p));

    //
    return Arrays.asList(resolverJob.resolveToUrlArray());
  }
}

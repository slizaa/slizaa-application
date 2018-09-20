package org.slizaa.server.extensions.impl;

import org.slizaa.core.mvnresolver.MvnResolverServiceFactoryFactory;
import org.slizaa.core.mvnresolver.api.IMvnResolverService;
import org.slizaa.core.mvnresolver.api.IMvnResolverServiceFactory;
import org.slizaa.server.extensions.IExtension;

import java.net.URL;

public class MavenBasedExtension implements IExtension {

  @Override
  public URL[] resolvedArtifactsToInstall() {


    //
    IMvnResolverServiceFactory resolverServiceFactory = MvnResolverServiceFactoryFactory
        .createNewResolverServiceFactory();

    //
    IMvnResolverService mvnResolverService = resolverServiceFactory.newMvnResolverService().create();

    //
    URL[] resolvedArtifacts = mvnResolverService.newMvnResolverJob()
        .withDependency("org.slizaa.scanner.neo4j:org.slizaa.scanner.neo4j.importer:1.0.0-SNAPSHOT")
        .withDependency("org.slizaa.scanner.neo4j:org.slizaa.scanner.neo4j.graphdbfactory:1.0.0-SNAPSHOT")
        .withDependency("org.slizaa.jtype:org.slizaa.jtype.scanner:1.0.0-SNAPSHOT")
        .withDependency("org.slizaa.jtype:org.slizaa.jtype.scanner.apoc:1.0.0-SNAPSHOT")
        .withDependency("org.slizaa.jtype:org.slizaa.jtype.hierarchicalgraph:1.0.0-SNAPSHOT")
        .withExclusionPattern("*:org.slizaa.scanner.core.spi-api").withExclusionPattern("*:jdk.tools")
        .resolveToUrlArray();

    return resolvedArtifacts;
  }
}

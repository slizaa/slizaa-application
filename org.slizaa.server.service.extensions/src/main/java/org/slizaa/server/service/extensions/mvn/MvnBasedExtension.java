package org.slizaa.server.service.extensions.mvn;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slizaa.core.mvnresolver.MvnResolverServiceFactoryFactory;
import org.slizaa.core.mvnresolver.api.IMvnResolverService;
import org.slizaa.core.mvnresolver.api.IMvnResolverServiceFactory;
import org.slizaa.server.service.extensions.ExtensionIdentifier;
import org.slizaa.server.service.extensions.IExtension;
import org.slizaa.server.service.extensions.Version;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * see:https://github.com/FasterXML/jackson-docs/wiki/JacksonPolymorphicDeserialization
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
public class MvnBasedExtension extends ExtensionIdentifier implements IExtension {

    /* - */
	@JsonProperty("dependencies")
    private List<MvnDependency> _dependencies;

    /**
     * Creates a new instance of type {@link MvnBasedExtension}.
     */
    public MvnBasedExtension(String symbolicName, Version version) {
        super(symbolicName, version);

        _dependencies = new ArrayList<>();
    }

    protected MvnBasedExtension() {
        super();

        _dependencies = new ArrayList<>();
    }
    
    /**
     * @param mvnDependency
     * @return
     */
    public MvnBasedExtension withDependency(MvnDependency mvnDependency) {
        _dependencies.add(checkNotNull(mvnDependency));
        return this;
    }

    /**
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
        _dependencies.forEach(dep -> resolverJob.withDependency(dep.getDependency()).withExclusionPatterns(dep.getExclusionPatterns().toArray(new String[0])));
        //
        return Arrays.asList(resolverJob.resolveToUrlArray());
    }
}

package org.slizaa.server.service.slizaa.internal;

import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IMappingProvider;
import org.slizaa.scanner.api.cypherregistry.ICypherStatementRegistry;
import org.slizaa.scanner.api.graphdb.IGraphDbFactory;
import org.slizaa.scanner.api.importer.IModelImporterFactory;
import org.slizaa.scanner.spi.parser.IParserFactory;
import org.slizaa.server.service.backend.ISlizaaServerBackend;
import org.slizaa.server.service.extensions.IExtension;
import org.slizaa.server.service.extensions.IExtensionIdentifier;
import org.slizaa.server.service.extensions.IExtensionService;
import org.slizaa.server.service.slizaa.ISlizaaService;
import org.slizaa.server.service.slizaa.ISystemAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
@Component
public class SlizaaComponent implements ISlizaaService, SystemAnalysis.SystemAnalysisContext {

    //
    {
        org.slizaa.hierarchicalgraph.core.model.CustomFactoryStandaloneSupport.registerCustomHierarchicalgraphFactory();
        org.slizaa.hierarchicalgraph.graphdb.model.CustomFactoryStandaloneSupport.registerCustomHierarchicalgraphFactory();
    }

    //
    private static final Logger logger = LoggerFactory.getLogger(SlizaaComponent.class);

    // TODO: -> root.database.directory
    @Value("${database.directory:}")
    private String _databaseDirectoryPath;

    /**
     * -
     */
    @Autowired
    private ISlizaaServerBackend _slizaaServerBackend;

    /**
     * -
     */
    @Autowired
    private IExtensionService _extensionService;

    /**
     * -
     */
    private ExecutorService _executorService;

    /**
     * <p>
     * </p>
     */
    @PostConstruct
    public void initialize() {

        //
//    try {
//      test();
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
    }

    @PreDestroy
    public void dispose() throws InterruptedException {


        this._executorService.shutdown();
        this._executorService.awaitTermination(5, TimeUnit.SECONDS);
    }

    /**
     * @return
     */
    @Override
    public boolean isBackendConfigured() {
        return _slizaaServerBackend.isConfigured();
    }

    @Override
    public List<IExtension> getInstalledExtensions() {
        return _slizaaServerBackend.getInstalledExtensions();
    }

    @Override
    public List<IExtension> installExtensions(List<IExtensionIdentifier> extensionIdentifiers) {

        //
        List<IExtension> extensionsToInstall = this._extensionService.getExtensions(extensionIdentifiers);

        //
        List<IExtension> installedExtensions = this._slizaaServerBackend.getInstalledExtensions();

        //
        List<IExtension> newExtensions = ListUtils.subtract(extensionsToInstall, installedExtensions);

        //
        _slizaaServerBackend.installExtensions(newExtensions);

        //
        return newExtensions;
    }

    @Override
    public List<IExtension> uninstallExtensions(List<IExtensionIdentifier> extensionIds) {
        // TODO
        return Collections.emptyList();
    }

    @Override
    public IExtensionService getExtensionService() {
        return _extensionService;
    }

    @Override
    public List<ISystemAnalysis> getSystemAnalyses() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public ClassLoader getCurrentExtensionClassLoader() {
        return isBackendConfigured() ?
                _slizaaServerBackend.getCurrentExtensionClassLoader() : null;
    }

    @Override
    public IGraphDbFactory getGraphDbFactory() {
        return _slizaaServerBackend.getGraphDbFactory();
    }

    @Override
    public ExecutorService getExecutorService() {
        return _executorService;
    }

    @Override
    public IModelImporterFactory getModelImporterFactory() {
        return _slizaaServerBackend.getModelImporterFactory();
    }

    @Override
    public List<IParserFactory> getParserFactories() {
        return _slizaaServerBackend.getParserFactories();
    }

    @Override
    public ICypherStatementRegistry getCypherStatementRegistry() {
        return _slizaaServerBackend.getCypherStatementRegistry();
    }

    @Override
    public List<IMappingProvider> getMappingProviders() {
        return _slizaaServerBackend.getMappingProviders();
    }
}

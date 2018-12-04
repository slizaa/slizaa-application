package org.slizaa.server.service.slizaa.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slizaa.core.boltclient.IBoltClient;
import org.slizaa.core.boltclient.IBoltClientFactory;
import org.slizaa.core.progressmonitor.DefaultProgressMonitor;
import org.slizaa.hierarchicalgraph.core.model.HGRootNode;
import org.slizaa.hierarchicalgraph.graphdb.mapping.service.IMappingService;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IMappingProvider;
import org.slizaa.scanner.api.graphdb.IGraphDb;
import org.slizaa.scanner.api.importer.IModelImporter;
import org.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProvider;
import org.slizaa.server.service.backend.ISlizaaServerBackend;
import org.slizaa.server.service.extensions.IExtension;
import org.slizaa.server.service.extensions.IExtensionIdentifier;
import org.slizaa.server.service.extensions.IExtensionService;
import org.slizaa.server.service.slizaa.ISlizaaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
@Component
public class SlizaaComponent implements ISlizaaService {

    //
    {
        org.slizaa.hierarchicalgraph.core.model.CustomFactoryStandaloneSupport.registerCustomHierarchicalgraphFactory();
        org.slizaa.hierarchicalgraph.graphdb.model.CustomFactoryStandaloneSupport.registerCustomHierarchicalgraphFactory();
    }

    //
    private static final Logger logger = LoggerFactory.getLogger(SlizaaComponent.class);

    //
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


    // TODO: Move -- START
    /**
     * -
     */
    private ILabelDefinitionProvider _labelDefinitionProvider;

    /**
     * -
     */
    private IGraphDb _graphDb;

    /**
     * -
     */
    private HGRootNode _rootNode;

    /**
     * -
     */
    private IBoltClient _boltClient;

    /**
     * -
     */
    private File _databaseDirectory;
    // TODO: Move -- STOP

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

        //
        this._boltClient.disconnect();
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
        List<IExtension> extensions = this._extensionService.getExtensions(extensionIdentifiers);
        _slizaaServerBackend.installExtensions(extensions);
        return  extensions;
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

    /**
     * @return
     */
    public ClassLoader getBackendClassLoader() {

        //
        return isBackendConfigured() ?
                _slizaaServerBackend.getCurrentExtensionClassLoader() : null;
    }

    // TODO: MOVE //

    /**
     * <p>
     * </p>
     *
     * @return
     */
    public HGRootNode getRootNode() {
        return this._rootNode;
    }

    /**
     * <p>
     * </p>
     *
     * @throws Exception
     */
    public void test() throws Exception {

        //
        if (_databaseDirectoryPath == null) {
            _databaseDirectory = new File(_databaseDirectoryPath);
            if (!_databaseDirectory.exists()) {
                if (!_databaseDirectory.mkdirs()) {
                    _databaseDirectory = null;
                }
            } else {
                if (_databaseDirectory.isFile()) {
                    _databaseDirectory = null;
                }
            }
        }

        //
        if (_databaseDirectory == null) {
            // TODO
            _databaseDirectory = Files.createTempDirectory("_slizaa_Temp").toFile();
        }

        //
        logger.info("Creating SlizaaComponent.");

        //
        if (this._databaseDirectory.exists() && this._databaseDirectory.list().length > 0) {

            // FUCK ME!
            Thread.currentThread().setContextClassLoader(this._slizaaServerBackend.getCurrentExtensionClassLoader());

            _graphDb = this._slizaaServerBackend.getGraphDbFactory().newGraphDb(5001, this._databaseDirectory).create();
        }
        //
        else {
            parseAndStartDatabase();
        }

        //
        this._executorService = Executors.newFixedThreadPool(10);
        IBoltClientFactory boltClientFactory = IBoltClientFactory.newInstance(this._executorService);
        this._boltClient = boltClientFactory.createBoltClient("bolt://localhost:5001");
        this._boltClient.connect();

        //
        IMappingService mappingService = IMappingService.createHierarchicalgraphMappingService();
        IMappingProvider mappingProvider = this._slizaaServerBackend.getMappingProviders().get(0);

        //
        this._rootNode = mappingService.convert(mappingProvider, this._boltClient,
                new DefaultProgressMonitor("Mapping", 100, DefaultProgressMonitor.consoleLogger()));

        //
        _labelDefinitionProvider = mappingProvider.getLabelDefinitionProvider();
    }

    /**
     * <p>
     * </p>
     *
     * @throws IOException
     */
    public void parseAndStartDatabase() throws IOException {

        //
        MvnBasedContentDefinitionProvider contentDefinitionProvider = new MvnBasedContentDefinitionProvider();
        contentDefinitionProvider.addArtifact("org.springframework", "spring-core", "5.0.9.RELEASE");
        contentDefinitionProvider.addArtifact("org.springframework", "spring-context", "5.0.9.RELEASE");
        contentDefinitionProvider.addArtifact("org.springframework", "spring-beans", "5.0.9.RELEASE");

        //
        if (!_databaseDirectory.exists()) {
            _databaseDirectory.mkdirs();
        }

        // delete all contained files
        Files.walk(this._databaseDirectory.toPath(), FileVisitOption.FOLLOW_LINKS).sorted(Comparator.reverseOrder())
                .map(Path::toFile).forEach(File::delete);

        // FUCK ME!
        Thread.currentThread().setContextClassLoader(this._slizaaServerBackend.getCurrentExtensionClassLoader());

        IModelImporter modelImporter = this._slizaaServerBackend.getModelImporterFactory()
                .createModelImporter(contentDefinitionProvider,
                        this._databaseDirectory, _slizaaServerBackend.getParserFactories(),
                        this._slizaaServerBackend.getCypherStatementRegistry().getAllStatements());

        // parse the model
        modelImporter.parse(new DefaultProgressMonitor("Parse", 100, DefaultProgressMonitor.consoleLogger()),
                () -> this._slizaaServerBackend.getGraphDbFactory().newGraphDb(5001, this._databaseDirectory).create());

        //
        _graphDb = modelImporter.getGraphDb();
    }
}

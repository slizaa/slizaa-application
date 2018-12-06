package org.slizaa.server.service.slizaa.internal;

import org.slizaa.core.boltclient.IBoltClient;
import org.slizaa.core.boltclient.IBoltClientFactory;
import org.slizaa.core.progressmonitor.DefaultProgressMonitor;
import org.slizaa.hierarchicalgraph.core.model.HGRootNode;
import org.slizaa.hierarchicalgraph.graphdb.mapping.service.IMappingService;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IMappingProvider;
import org.slizaa.scanner.api.cypherregistry.ICypherStatementRegistry;
import org.slizaa.scanner.api.graphdb.IGraphDb;
import org.slizaa.scanner.api.graphdb.IGraphDbFactory;
import org.slizaa.scanner.api.importer.IModelImporter;
import org.slizaa.scanner.api.importer.IModelImporterFactory;
import org.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProvider;
import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import org.slizaa.scanner.spi.parser.IParserFactory;
import org.slizaa.server.service.slizaa.ISystemAnalysis;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class SystemAnalysis implements ISystemAnalysis {

    public interface SystemAnalysisContext {

        ClassLoader getCurrentExtensionClassLoader();

        IGraphDbFactory getGraphDbFactory();

        ExecutorService getExecutorService();

        IModelImporterFactory getModelImporterFactory();

        List<IParserFactory> getParserFactories();

        ICypherStatementRegistry getCypherStatementRegistry();

        List<IMappingProvider> getMappingProviders();
    }

    private SystemAnalysisContext context;

    private IContentDefinitionProvider contentDefinitionProvider;

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

    public SystemAnalysis(SystemAnalysisContext context) {
        this.context = context;
    }

    // TODO: Move -- STOP
    @Override
    public void addContentDefinitionProvider(IContentDefinitionProvider contentDefinitionProvider) {
        this.contentDefinitionProvider = contentDefinitionProvider;
    }

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
//        if (_databaseDirectoryPath == null) {
//            _databaseDirectory = new File(_databaseDirectoryPath);
//            if (!_databaseDirectory.exists()) {
//                if (!_databaseDirectory.mkdirs()) {
//                    _databaseDirectory = null;
//                }
//            } else {
//                if (_databaseDirectory.isFile()) {
//                    _databaseDirectory = null;
//                }
//            }
//        }

        //
        if (_databaseDirectory == null) {
            // TODO
            _databaseDirectory = Files.createTempDirectory("_slizaa_Temp").toFile();
        }

        //
        if (this._databaseDirectory.exists() && this._databaseDirectory.list().length > 0) {

            // FUCK ME!
            Thread.currentThread().setContextClassLoader(this.context.getCurrentExtensionClassLoader());

            _graphDb = this.context.getGraphDbFactory().newGraphDb(5001, this._databaseDirectory).create();
        }
        //
        else {
            parseAndStartDatabase();
        }

        //
        IBoltClientFactory boltClientFactory = IBoltClientFactory.newInstance(this.context.getExecutorService());
        this._boltClient = boltClientFactory.createBoltClient("bolt://localhost:5001");
        this._boltClient.connect();

        //
        IMappingService mappingService = IMappingService.createHierarchicalgraphMappingService();
        IMappingProvider mappingProvider = this.context.getMappingProviders().get(0);

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
        Thread.currentThread().setContextClassLoader(this.context.getCurrentExtensionClassLoader());

        IModelImporter modelImporter = this.context.getModelImporterFactory()
                .createModelImporter(contentDefinitionProvider,
                        this._databaseDirectory, context.getParserFactories(),
                        this.context.getCypherStatementRegistry().getAllStatements());

        // parse the model
        modelImporter.parse(new DefaultProgressMonitor("Parse", 100, DefaultProgressMonitor.consoleLogger()),
                () -> this.context.getGraphDbFactory().newGraphDb(5001, this._databaseDirectory).create());

        //
        _graphDb = modelImporter.getGraphDb();
    }
    public void shutdown() {

        this._boltClient.disconnect();
    }
}

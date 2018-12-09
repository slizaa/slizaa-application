package org.slizaa.server.service.slizaa.internal;

import org.slizaa.core.boltclient.IBoltClient;
import org.slizaa.core.boltclient.IBoltClientFactory;
import org.slizaa.core.progressmonitor.DefaultProgressMonitor;
import org.slizaa.hierarchicalgraph.core.model.HGRootNode;
import org.slizaa.hierarchicalgraph.graphdb.mapping.service.IMappingService;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IMappingProvider;
import org.slizaa.scanner.api.graphdb.IGraphDb;
import org.slizaa.scanner.api.importer.IModelImporter;
import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import org.slizaa.server.service.slizaa.IStructureDatabase;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public class StructureDatabase implements IStructureDatabase {

  //
  private String _id;

  //
  private SlizaaComponent _slizaaComponent;

  //
  private IContentDefinitionProvider _contentDefinitionProvider;

  //
  private ILabelDefinitionProvider _labelDefinitionProvider;

  //
  private IGraphDb _graphDb;

  //
  private HGRootNode _rootNode;

  //
  private IBoltClient _boltClient;

  //
  private File _databaseDirectory;

  /**
   * @param slizaaComponent
   */
  public StructureDatabase(String id, SlizaaComponent slizaaComponent) {
    this._id = checkNotNull(id);
    this._slizaaComponent = checkNotNull(slizaaComponent);
  }

  @Override
  public String getIdentifier() {
    return _id;
  }

  /**
   * @param contentDefinitionProvider
   */
  @Override
  public void addContentDefinitionProvider(IContentDefinitionProvider contentDefinitionProvider) {
    this._contentDefinitionProvider = contentDefinitionProvider;
  }

  @Override
  public boolean hasContentDefinitionProvider() {
    return _contentDefinitionProvider != null;
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
  public void mapSystem() throws Exception {

    //
    IBoltClientFactory boltClientFactory = IBoltClientFactory.newInstance(this._slizaaComponent.getExecutorService());
    this._boltClient = boltClientFactory.createBoltClient("bolt://localhost:5001");
    this._boltClient.connect();

    //
    IMappingService mappingService = IMappingService.createHierarchicalgraphMappingService();
    IMappingProvider mappingProvider = this._slizaaComponent.getSlizaaServerBackend().getMappingProviders().get(0);

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
    if (_databaseDirectory == null) {
      // TODO
      _databaseDirectory = Files.createTempDirectory("_slizaa_Temp").toFile();
    }

    //
    if (!_databaseDirectory.exists()) {
      _databaseDirectory.mkdirs();
    }

    // delete all contained files
    Files.walk(this._databaseDirectory.toPath(), FileVisitOption.FOLLOW_LINKS).sorted(Comparator.reverseOrder())
        .map(Path::toFile).forEach(File::delete);

    // FUCK ME!
    Thread.currentThread().setContextClassLoader(this._slizaaComponent.getCurrentExtensionClassLoader());

    IModelImporter modelImporter = this._slizaaComponent.getSlizaaServerBackend().getModelImporterFactory()
        .createModelImporter(_contentDefinitionProvider,
            this._databaseDirectory, _slizaaComponent.getSlizaaServerBackend().getParserFactories(),
            this._slizaaComponent.getSlizaaServerBackend().getCypherStatementRegistry().getAllStatements());

    // parse the model
    modelImporter.parse(new DefaultProgressMonitor("Parse", 100, DefaultProgressMonitor.consoleLogger()),
        () -> this._slizaaComponent.getGraphDbFactory().newGraphDb(5001, this._databaseDirectory).create());

    //
    _graphDb = modelImporter.getGraphDb();
  }

  public void shutdown() {
    this._graphDb.shutdown();
    this._boltClient.disconnect();
  }
}

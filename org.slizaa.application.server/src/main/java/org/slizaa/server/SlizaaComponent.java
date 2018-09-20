package org.slizaa.server;

import org.slizaa.core.boltclient.IBoltClient;
import org.slizaa.core.boltclient.IBoltClientFactory;
import org.slizaa.core.mvnresolver.MvnResolverServiceFactoryFactory;
import org.slizaa.core.mvnresolver.api.IMvnResolverService;
import org.slizaa.core.mvnresolver.api.IMvnResolverServiceFactory;
import org.slizaa.hierarchicalgraph.core.model.HGRootNode;
import org.slizaa.hierarchicalgraph.graphdb.mapping.service.IMappingService;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IMappingProvider;
import org.slizaa.scanner.core.api.cypherregistry.ICypherStatement;
import org.slizaa.scanner.core.api.cypherregistry.ICypherStatementRegistry;
import org.slizaa.scanner.core.api.graphdb.IGraphDb;
import org.slizaa.scanner.core.api.graphdb.IGraphDbFactory;
import org.slizaa.scanner.core.api.importer.IModelImporter;
import org.slizaa.scanner.core.api.importer.IModelImporterFactory;
import org.slizaa.scanner.core.contentdefinition.MvnBasedContentDefinitionProvider;
import org.slizaa.scanner.core.cypherregistry.CypherRegistryUtils;
import org.slizaa.scanner.core.cypherregistry.CypherStatementRegistry;
import org.slizaa.scanner.core.spi.parser.IParserFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
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
public class SlizaaComponent {


  {
    org.slizaa.hierarchicalgraph.core.model.CustomFactoryStandaloneSupport.registerCustomHierarchicalgraphFactory();
    org.slizaa.hierarchicalgraph.graphdb.model.CustomFactoryStandaloneSupport.registerCustomHierarchicalgraphFactory();
  }

  @Value("${database.directory}")
  private File                     _databaseDirectory;

  /** - */
  private ClassLoader              _extensionsClassLoader;

  /** - */
  private IModelImporterFactory    _modelImporterFactory;

  /** - */
  private IGraphDbFactory          _graphDbFactory;

  /** - */
  private IParserFactory           _parserFactory;

  /** - */
  private List<IMappingProvider>   _mappingProviders;

  /** - */
  private ICypherStatementRegistry _cypherStatementRegistry;

  /** - */
  private ILabelDefinitionProvider _labelDefinitionProvider;

  /** - */
  private IGraphDb _graphDb;

  /** - */
  private HGRootNode               _rootNode;

  /** - */
  private IBoltClient              _boltClient;

  /** - */
  private ExecutorService          _executorService;

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
   */
  @PostConstruct
  public void initialize() {

    //
    this._extensionsClassLoader = dynamicallyLoadExtensions();

    //
    ServiceLoader<IModelImporterFactory> serviceLoader = ServiceLoader.load(IModelImporterFactory.class,
        this._extensionsClassLoader);
    this._modelImporterFactory = serviceLoader.iterator().next();

    //
    ServiceLoader<IGraphDbFactory> serviceLoader_2 = ServiceLoader.load(IGraphDbFactory.class,
        this._extensionsClassLoader);
    this._graphDbFactory = serviceLoader_2.iterator().next();

    //
    ServiceLoader<IParserFactory> serviceLoader_3 = ServiceLoader.load(IParserFactory.class,
        this._extensionsClassLoader);
    this._parserFactory = serviceLoader_3.iterator().next();

    //
    ServiceLoader<IMappingProvider> serviceLoader_4 = ServiceLoader.load(IMappingProvider.class,
        this._extensionsClassLoader);
    this._mappingProviders = new ArrayList<>();
    serviceLoader_4.iterator().forEachRemaining(mp -> this._mappingProviders.add(mp));

    // System.out.println(this._modelImporterFactory);
    // System.out.println(this._graphDbFactory);
    // System.out.println(this._parserFactory);
    // System.out.println(this._mappingProviders);

    //
    List<ICypherStatement> result = CypherRegistryUtils.getCypherStatementsFromClasspath(_extensionsClassLoader);

    this._cypherStatementRegistry = new CypherStatementRegistry(() -> {
      return result;
    });

    //
    this._cypherStatementRegistry.rescan();

    //
    try {
      test();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @PreDestroy
  public void dispose() throws InterruptedException {

    //
    this._boltClient.disconnect();
    this._executorService.shutdown();
    this._executorService.awaitTermination(5, TimeUnit.SECONDS);
  }

  /**
   * <p>
   * </p>
   *
   * @throws Exception
   */
  public void test() throws Exception {

    //
    if (this._databaseDirectory.exists() && this._databaseDirectory.list().length > 0) {
      // FUCK ME!
      Thread.currentThread().setContextClassLoader(this._extensionsClassLoader);

      this._graphDbFactory.newGraphDb(5001, this._databaseDirectory).create();
    } else {
      parseAndStartDatabase();
    }

    //
    this._executorService = Executors.newFixedThreadPool(10);
    IBoltClientFactory boltClientFactory = IBoltClientFactory.newInstance(this._executorService);

    //
    this._boltClient = boltClientFactory.createBoltClient("bolt://localhost:5001");
    this._boltClient.connect();

    //
    IMappingService mappingService = IMappingService.createHierarchicalgraphMappingService();
    IMappingProvider mappingProvider = this._mappingProviders.get(0);
    this._rootNode = mappingService.convert(mappingProvider, this._boltClient, new SlizaaTestProgressMonitor());

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

    // delete all contained files
    Files.walk(this._databaseDirectory.toPath(), FileVisitOption.FOLLOW_LINKS).sorted(Comparator.reverseOrder())
        .map(Path::toFile).forEach(File::delete);

    // FUCK ME!
    Thread.currentThread().setContextClassLoader(this._extensionsClassLoader);

    IModelImporter modelImporter = this._modelImporterFactory.createModelImporter(contentDefinitionProvider,
        this._databaseDirectory, Collections.singletonList(this._parserFactory),
        this._cypherStatementRegistry.getAllStatements());

    modelImporter.parse(new SlizaaTestProgressMonitor(),
        () -> this._graphDbFactory.newGraphDb(5001, this._databaseDirectory).create());

    //
    _graphDb = modelImporter.getGraphDb();
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  private ClassLoader dynamicallyLoadExtensions() {

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

    //
    return new URLClassLoader(resolvedArtifacts, SlizaaComponent.class.getClassLoader());
  }
}

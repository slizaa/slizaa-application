package org.slizaa.server.service.slizaa.internal.structuredatabase;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProvider;
import org.slizaa.server.service.backend.EnableBackendServiceModule;
import org.slizaa.server.service.configuration.EnableConfigurationModule;
import org.slizaa.server.service.extensions.EnableExtensionsModule;
import org.slizaa.server.service.extensions.IExtensionService;
import org.slizaa.server.service.slizaa.EnableSlizaaServiceModule;
import org.slizaa.server.service.slizaa.IHierarchicalGraph;
import org.slizaa.server.service.slizaa.IStructureDatabase;
import org.slizaa.server.service.slizaa.internal.SlizaaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@Configuration
@EnableBackendServiceModule
@EnableExtensionsModule
@EnableConfigurationModule
@EnableSlizaaServiceModule
public class StructureDatabaseTest {

	@ClassRule
	public static TemporaryFolder folder = new TemporaryFolder();

	@BeforeClass
	public static void before() throws Exception {
		System.setProperty("configuration.rootDirectory", folder.newFolder().getAbsolutePath());
		System.setProperty("slizaa.rootDatabaseDirectory", folder.newFolder().getAbsolutePath());
	}

	@Autowired
	private SlizaaServiceImpl _slizaaService;

	private IStructureDatabase _structureDatabase;

	@Before
	public void setUp() throws Exception {

		String STRUCTURE_DATABASE_NAME = "HURZ";

		if (! _slizaaService.getBackendService().hasInstalledExtensions()) {
			_slizaaService.getBackendService().installExtensions(_slizaaService.getExtensionService().getExtensions());
		}

		if (! _slizaaService.hasStructureDatabase(STRUCTURE_DATABASE_NAME)) {
			
			// create a new database
			_structureDatabase = _slizaaService.newStructureDatabase(STRUCTURE_DATABASE_NAME);
			
			// configure
			MvnBasedContentDefinitionProvider mvnBasedContentDefinitionProvider = new MvnBasedContentDefinitionProvider();
			mvnBasedContentDefinitionProvider
					.addArtifact("org.springframework.statemachine:spring-statemachine-core:2.0.3.RELEASE");
			_structureDatabase.setContentDefinitionProvider(mvnBasedContentDefinitionProvider);
			
			// and parse
			_structureDatabase.parse(true);
		} 
		//
		else {
			_structureDatabase = _slizaaService.getStructureDatabase(STRUCTURE_DATABASE_NAME);
			_structureDatabase.start();
		}
		
		IHierarchicalGraph hierarchicalGraph = _structureDatabase.createNewHierarchicalGraph("HG");
		System.out.println(hierarchicalGraph.getRootNode());

		//
		assertThat(_structureDatabase).isNotNull();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void getIdentifier() {

		// TODO
	}
}
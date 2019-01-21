package org.slizaa.server.service.slizaa.internal.structuredatabase;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProvider;
import org.slizaa.server.service.backend.EnableBackendServiceModule;
import org.slizaa.server.service.configuration.EnableConfigurationModule;
import org.slizaa.server.service.extensions.EnableExtensionsModule;
import org.slizaa.server.service.extensions.IExtensionService;
import org.slizaa.server.service.slizaa.IHierarchicalGraph;
import org.slizaa.server.service.slizaa.IStructureDatabase;
import org.slizaa.server.service.slizaa.internal.SlizaaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Configuration
@ComponentScan(basePackageClasses = SlizaaServiceImpl.class)
@EnableBackendServiceModule
@EnableExtensionsModule
@EnableConfigurationModule
public class StructureDatabaseTest {

	private static final String STRUCTURE_DATABASE_NAME = "HURZ";

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	//
	@Autowired
	private SlizaaServiceImpl _slizaaService;

	@Autowired
	private IExtensionService _extensionService;

	//
	private IStructureDatabase _structureDatabase;

	@Before
	public void setUp() throws Exception {

		//
		_slizaaService.setDatabaseDirectory(folder.newFolder());
		
		//
		if (! _slizaaService.getBackendService().hasInstalledExtensions()) {
			_slizaaService.getBackendService().installExtensions(_extensionService.getExtensions());
		}

		//
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
		assertThat(_structureDatabase.getIdentifier()).isEqualTo(STRUCTURE_DATABASE_NAME);
	}
}
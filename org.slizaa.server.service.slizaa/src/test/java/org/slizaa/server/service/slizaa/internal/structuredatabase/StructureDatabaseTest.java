package org.slizaa.server.service.slizaa.internal.structuredatabase;

import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProvider;
import org.slizaa.server.service.slizaa.IStructureDatabase;
import org.slizaa.server.service.slizaa.internal.AbstractSlizaaServiceTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
public class StructureDatabaseTest extends AbstractSlizaaServiceTest {

	public static final String STRUCTURE_DATABASE_NAME = "TEST_STRUCTURE_DATABASE";

	private IStructureDatabase structureDatabase;

	@Before
	public void before() {
		if (!slizaaService().getBackendService().hasInstalledExtensions()) {
			slizaaService().getBackendService()
					.installExtensions(slizaaService().getExtensionService().getExtensions());
		}
		assertThat(slizaaService().hasStructureDatabase(STRUCTURE_DATABASE_NAME)).isFalse();
	}

	@After
	public void after() {
		structureDatabase.dispose();
		assertThat(slizaaService().hasStructureDatabase(STRUCTURE_DATABASE_NAME)).isFalse();
	}

	@Test
	public void test_1() throws IOException {

		// create a new database
		structureDatabase = slizaaService().newStructureDatabase(STRUCTURE_DATABASE_NAME);
		assertThat(structureDatabase.isRunning()).isFalse();

		//
		structureDatabase.setContentDefinitionProvider(createContentDefinitionProvider());

		// and parse
		structureDatabase.parse(true);

		//
		assertThat(structureDatabase.isRunning());
	}

	@Test
	public void test_2() throws IOException {

		assertThat(slizaaService().hasStructureDatabase(STRUCTURE_DATABASE_NAME)).isFalse();

		// create a new database
		structureDatabase = slizaaService().newStructureDatabase(STRUCTURE_DATABASE_NAME);

		// configure
		structureDatabase.setContentDefinitionProvider(createContentDefinitionProvider());

		// and parse
		structureDatabase.parse(true);

		//
		assertThat(structureDatabase.isRunning());
	}

	/**
	 *
	 * @return
	 */
	private MvnBasedContentDefinitionProvider createContentDefinitionProvider() {
		MvnBasedContentDefinitionProvider mvnBasedContentDefinitionProvider = new MvnBasedContentDefinitionProvider();
		mvnBasedContentDefinitionProvider
				.addArtifact("org.springframework.statemachine:spring-statemachine-core:2.0.3.RELEASE");
		return mvnBasedContentDefinitionProvider;
	}
}
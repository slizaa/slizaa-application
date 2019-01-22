package org.slizaa.server.service.slizaa.internal.structuredatabase;

import java.io.IOException;

import org.junit.Test;
import org.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProvider;
import org.slizaa.server.service.slizaa.IStructureDatabase;
import org.slizaa.server.service.slizaa.internal.AbstractSlizaaServiceTest;

/**
 * 
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
public class StructureDatabaseTest extends AbstractSlizaaServiceTest {

	@Test
	public void test_1() throws IOException {

		if (!slizaaService().getBackendService().hasInstalledExtensions()) {
			slizaaService().getBackendService()
					.installExtensions(slizaaService().getExtensionService().getExtensions());
		}

		if (!slizaaService().hasStructureDatabase(STRUCTURE_DATABASE_NAME)) {

			// create a new database
			IStructureDatabase structureDatabase = slizaaService().newStructureDatabase(STRUCTURE_DATABASE_NAME);

			// configure
			MvnBasedContentDefinitionProvider mvnBasedContentDefinitionProvider = new MvnBasedContentDefinitionProvider();
			mvnBasedContentDefinitionProvider
					.addArtifact("org.springframework.statemachine:spring-statemachine-core:2.0.3.RELEASE");
			structureDatabase.setContentDefinitionProvider(mvnBasedContentDefinitionProvider);

			// and parse
			structureDatabase.parse(true);
		}
		//
		else {
			IStructureDatabase structureDatabase = slizaaService().getStructureDatabase(STRUCTURE_DATABASE_NAME);
			structureDatabase.start();
		}
	}
}
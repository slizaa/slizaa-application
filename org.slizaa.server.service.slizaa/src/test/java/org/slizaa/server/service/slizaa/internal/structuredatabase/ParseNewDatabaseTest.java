package org.slizaa.server.service.slizaa.internal.structuredatabase;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slizaa.server.service.slizaa.IGraphDatabase;
import org.slizaa.server.service.slizaa.internal.AbstractSlizaaServiceTest;

/**
 * 
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
public class ParseNewDatabaseTest extends AbstractSlizaaServiceTest {

  public static final String STRUCTURE_DATABASE_NAME = "TEST_STRUCTURE_DATABASE";

  private IGraphDatabase     structureDatabase;

  @Before
  public void before() {
    if (!slizaaService().getBackendService().hasInstalledExtensions()) {
      slizaaService().getBackendService().installExtensions(slizaaService().getExtensionService().getExtensions());
    }
    assertThat(slizaaService().hasStructureDatabase(STRUCTURE_DATABASE_NAME)).isFalse();
    assertThat(getDatabaseRootDirectory().listFiles()).hasSize(0);
  }

  @After
  public void after() {
    structureDatabase.terminate();
    assertThat(slizaaService().hasStructureDatabase(STRUCTURE_DATABASE_NAME)).isFalse();
  }

  @Test
  public void parseWithStart() throws IOException {

    // create a new database and parse with start
    structureDatabase = slizaaService().newGraphDatabase(STRUCTURE_DATABASE_NAME);
    structureDatabase.setContentDefinition(
        "org.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProviderFactory",
        "org.springframework.statemachine:spring-statemachine-core:2.0.3.RELEASE");
    structureDatabase.parse(true);

    //
    assertThat(structureDatabase.isRunning()).isTrue();
  }

  @Test
  public void parseWithoutStart() throws IOException {

    // create a new database and parse without start
    structureDatabase = slizaaService().newGraphDatabase(STRUCTURE_DATABASE_NAME);
    structureDatabase.setContentDefinition(
        "org.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProviderFactory",
        "org.springframework.statemachine:spring-statemachine-core:2.0.3.RELEASE");
    structureDatabase.parse(false);

    //
    assertThat(structureDatabase.isRunning()).isFalse();
  }

  @Test
  public void parseWithStartAndStop() throws IOException {

    // create a new database and parse without start
    structureDatabase = slizaaService().newGraphDatabase(STRUCTURE_DATABASE_NAME);
    structureDatabase.setContentDefinition(
        "org.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProviderFactory",
        "org.springframework.statemachine:spring-statemachine-core:2.0.3.RELEASE");
    structureDatabase.parse(true);
    assertThat(structureDatabase.isRunning()).isTrue();

    //
    structureDatabase.stop();
    assertThat(structureDatabase.isRunning()).isFalse();
  }

  @Test
  public void parseWithStartAndStopAndStart() throws IOException {

    // create a new database and parse without start
    structureDatabase = slizaaService().newGraphDatabase(STRUCTURE_DATABASE_NAME);
    structureDatabase.setContentDefinition(
        "org.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProviderFactory",
        "org.springframework.statemachine:spring-statemachine-core:2.0.3.RELEASE");
    structureDatabase.parse(true);
    assertThat(structureDatabase.isRunning()).isTrue();

    //
    structureDatabase.stop();
    assertThat(structureDatabase.isRunning()).isFalse();

    //
    structureDatabase.start();
    assertThat(structureDatabase.isRunning()).isTrue();
  }
}
package org.slizaa.server.service.slizaa.internal.structuredatabase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProvider;
import org.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProviderFactory;
import org.slizaa.server.service.slizaa.IGraphDatabase;
import org.slizaa.server.service.slizaa.internal.AbstractSlizaaServiceTest;
import org.slizaa.server.service.slizaa.internal.graphdatabase.GraphDatabaseTrigger;

/**
 * 
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
public class AllowedTransitionsOnDatabaseTest extends AbstractSlizaaServiceTest {

  public static final String STRUCTURE_DATABASE_NAME = "TEST_STRUCTURE_DATABASE";

  private IGraphDatabase     database;

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
    database.dispose();
    assertThat(slizaaService().hasStructureDatabase(STRUCTURE_DATABASE_NAME)).isFalse();
  }

  @Test
  public void test_INITIAL() throws IOException {

    // create a new database and parse with start
    database = slizaaService().newGraphDatabase(STRUCTURE_DATABASE_NAME);

    assertThat(database.getAllowedTrigger())
        .containsExactlyInAnyOrder(GraphDatabaseTrigger.SET_CONTENT_DEFINITION, GraphDatabaseTrigger.TERMINATE);
  }
  
  @Test
  public void test_SET_CONTENT_DEFINITION() throws IOException {

    // create a new database and parse with start
    database = slizaaService().newGraphDatabase(STRUCTURE_DATABASE_NAME);
    
    database.setContentDefinitionProvider(
        "org.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProviderFactory",
        "org.springframework.statemachine:spring-statemachine-core:2.0.3.RELEASE");

    assertThat(database.getAllowedTrigger())
        .containsExactlyInAnyOrder(GraphDatabaseTrigger.PARSE, GraphDatabaseTrigger.TERMINATE);
  }
  
  @Test
  public void test_RUNNING() throws IOException {

    // create a new database and parse with start
    database = slizaaService().newGraphDatabase(STRUCTURE_DATABASE_NAME);
    
    database.setContentDefinitionProvider(
        "org.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProviderFactory",
        "org.springframework.statemachine:spring-statemachine-core:2.0.3.RELEASE");
    
    database.parse(true);
    
    await().atMost(60, TimeUnit.SECONDS).until(() -> database.isRunning()); 

    assertThat(database.getAllowedTrigger())
        .containsExactlyInAnyOrder(GraphDatabaseTrigger.STOP, GraphDatabaseTrigger.TERMINATE);
  }
}
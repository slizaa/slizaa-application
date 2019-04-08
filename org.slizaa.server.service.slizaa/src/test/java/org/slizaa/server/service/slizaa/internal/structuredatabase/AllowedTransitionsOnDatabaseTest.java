package org.slizaa.server.service.slizaa.internal.structuredatabase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slizaa.server.service.slizaa.GraphDatabaseState;
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

    if (GraphDatabaseState.TERMINATED.equals(database.getState())) {
      database.terminate();
    }

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
    
    database.setContentDefinition(
        "org.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProviderFactory",
        "ant4eclipse:ant4eclipse:0.5.0.rc1");

    assertThat(database.getAllowedTrigger())
        .containsExactlyInAnyOrder(GraphDatabaseTrigger.PARSE, GraphDatabaseTrigger.TERMINATE);
  }
  
  @Test
  public void test_RUNNING() throws IOException {

    // create a new database and parse with start
    database = slizaaService().newGraphDatabase(STRUCTURE_DATABASE_NAME);
    
    database.setContentDefinition(
        "org.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProviderFactory",
        "ant4eclipse:ant4eclipse:0.5.0.rc1");
    
    database.parse(true);
    
    await().atMost(60, TimeUnit.SECONDS).until(() -> database.isRunning()); 

    assertThat(database.getAllowedTrigger())
        .containsExactlyInAnyOrder(GraphDatabaseTrigger.STOP, GraphDatabaseTrigger.TERMINATE);
  }

  @Test
  public void test_NOT_RUNNING() throws IOException {

    // create a new database and parse with start
    database = slizaaService().newGraphDatabase(STRUCTURE_DATABASE_NAME);

    database.setContentDefinition(
            "org.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProviderFactory",
            "ant4eclipse:ant4eclipse:0.5.0.rc1");

    database.parse(false);
    await().atMost(60, TimeUnit.SECONDS).until(() ->  GraphDatabaseState.NOT_RUNNING.equals(database.getState()));

    assertThat(database.getAllowedTrigger())
            .containsExactlyInAnyOrder(GraphDatabaseTrigger.START, GraphDatabaseTrigger.PARSE, GraphDatabaseTrigger.SET_CONTENT_DEFINITION, GraphDatabaseTrigger.TERMINATE);
  }

  @Test
  public void test_TERMINATED() throws IOException {

    // create a new database and parse with start
    database = slizaaService().newGraphDatabase(STRUCTURE_DATABASE_NAME);

    database.terminate();

    assertThat(database.getAllowedTrigger())
            .containsExactlyInAnyOrder();
  }
}
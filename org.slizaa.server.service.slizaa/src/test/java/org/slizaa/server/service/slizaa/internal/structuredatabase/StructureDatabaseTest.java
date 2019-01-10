package org.slizaa.server.service.slizaa.internal.structuredatabase;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.slizaa.server.service.backend.EnableBackendModule;
import org.slizaa.server.service.extensions.EnableExtensionsModule;
import org.slizaa.server.service.extensions.IExtensionService;
import org.slizaa.server.service.slizaa.ISlizaaService;
import org.slizaa.server.service.slizaa.IStructureDatabase;
import org.slizaa.server.service.slizaa.internal.SlizaaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@Configuration
@ComponentScan(basePackageClasses = SlizaaServiceImpl.class)
@EnableBackendModule
@EnableExtensionsModule
public class StructureDatabaseTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  //
  @Autowired
  private SlizaaServiceImpl _slizaaService;

  @Autowired
  private IExtensionService _extensionService;

  private IStructureDatabase _structureDatabase;

  @Before
  public void setUp() throws Exception {

    //
    _slizaaService.setDatabaseDirectory(folder.newFolder());
    _slizaaService.installExtensions(_extensionService.getExtensions());

    //
    _structureDatabase = _slizaaService.newStructureDatabase("HURZ");
    assertThat(_structureDatabase).isNotNull();
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void getIdentifier() {
    assertThat(_structureDatabase.getIdentifier()).isEqualTo("HURZ");
  }

  @Test
  public void setContentDefinitionProvider() {
  }

  @Test
  public void hasContentDefinitionProvider() {
  }

  @Test
  public void createNewMappedSystem() {
  }

  @Test
  public void disposeMappedSystem() {
  }

  @Test
  public void getMappedSystems() {
  }

  @Test
  public void parse() {
  }

  @Test
  public void start() {
  }

  @Test
  public void stop() {
  }

  @Test
  public void _start() {
  }

  @Test
  public void _stop() {
  }

  @Test
  public void _parse() {
  }
}
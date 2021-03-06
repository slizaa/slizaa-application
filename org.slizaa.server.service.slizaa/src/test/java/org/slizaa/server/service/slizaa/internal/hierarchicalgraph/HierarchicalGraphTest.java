package org.slizaa.server.service.slizaa.internal.hierarchicalgraph;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slizaa.hierarchicalgraph.core.algorithms.GraphUtils;
import org.slizaa.hierarchicalgraph.core.algorithms.IDependencyStructureMatrix;
import org.slizaa.hierarchicalgraph.core.model.HGNode;
import org.slizaa.hierarchicalgraph.core.model.HGRootNode;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;
import org.slizaa.server.service.slizaa.IHierarchicalGraph;
import org.slizaa.server.service.slizaa.IGraphDatabase;
import org.slizaa.server.service.slizaa.internal.AbstractSlizaaServiceTest;
import org.slizaa.server.service.slizaa.internal.SlizaaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class HierarchicalGraphTest extends AbstractSlizaaServiceTest {

  @Autowired
  private SlizaaServiceImpl _slizaaService;

  private IGraphDatabase    _structureDatabase;

  @Before
  public void setUp() throws Exception {

    String STRUCTURE_DATABASE_NAME = "HURZ";

    if (!_slizaaService.getBackendService().hasInstalledExtensions()) {
      _slizaaService.getBackendService().installExtensions(_slizaaService.getExtensionService().getExtensions());
    }

    if (!_slizaaService.hasStructureDatabase(STRUCTURE_DATABASE_NAME)) {

      // create a new database
      _structureDatabase = _slizaaService.newGraphDatabase(STRUCTURE_DATABASE_NAME);

      // configure
      _structureDatabase.setContentDefinition(
          "org.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProviderFactory",
          "org.springframework.statemachine:spring-statemachine-core:2.0.3.RELEASE");

      // and parse
      _structureDatabase.parse(true);
    }
    //
    else {
      _structureDatabase = _slizaaService.getGraphDatabase(STRUCTURE_DATABASE_NAME);
      _structureDatabase.start();
    }
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void hierarchicalGraphTest() {

    IHierarchicalGraph hierarchicalGraph = _structureDatabase.newHierarchicalGraph("HG");

    HGRootNode rootNode = hierarchicalGraph.getRootNode();

    ILabelDefinitionProvider labelDefinitionProvider = rootNode.getExtension(ILabelDefinitionProvider.class);

    List<HGNode> nodes = rootNode.getChildren().get(0).getChildren().get(0).getChildren().get(0).getChildren().get(0)
        .getChildren();

    IDependencyStructureMatrix dependencyStructureMatrix = GraphUtils.createDependencyStructureMatrix(nodes);
    for (int i = 0; i < nodes.size(); i++) {
      for (int j = 0; j < nodes.size(); j++) {
        System.out.println(i + " -> " + j + " :" + dependencyStructureMatrix.getWeight(i, j));
      }
    }
  }
}
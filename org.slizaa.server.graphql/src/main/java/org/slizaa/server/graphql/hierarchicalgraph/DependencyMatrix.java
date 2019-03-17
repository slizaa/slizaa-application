package org.slizaa.server.graphql.hierarchicalgraph;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slizaa.hierarchicalgraph.core.algorithms.IDependencyStructureMatrix;

public class DependencyMatrix {

  private List<Node>                       _orderedNodes;

  private int[][]                          _matrix;

  private List<StronglyConnectedComponent> _stronglyConnectedComponents;

  /**
   * 
   * @param dependencyStructureMatrix
   */
  public DependencyMatrix(IDependencyStructureMatrix dependencyStructureMatrix) {

    //
    this._orderedNodes = dependencyStructureMatrix.getOrderedNodes().stream().map(hgNode -> new Node(hgNode))
        .collect(Collectors.toList());

    //
    this._matrix = dependencyStructureMatrix.getMatrix();
    
    //
    this._stronglyConnectedComponents = dependencyStructureMatrix.getCycles().stream()
        .map(hgNodes -> new StronglyConnectedComponent(hgNodes, dependencyStructureMatrix.getOrderedNodes())).collect(Collectors.toList());


  }

  public List<Node> orderedNodes() {
    return this._orderedNodes;
  }

  public List<Cell> getCells() {
    List<Cell> result = new ArrayList<>();
    for (int i = 0; i < this._matrix.length; i++) {
      for (int j = 0; j < this._matrix[i].length; j++) {
        result.add(new Cell(i, j, this._matrix[i][j]));
      }
    }
    return result;
  }

  /**
   * 
   * @return
   */
  public List<StronglyConnectedComponent> getStronglyConnectedComponents() {
    return _stronglyConnectedComponents;
  }
}

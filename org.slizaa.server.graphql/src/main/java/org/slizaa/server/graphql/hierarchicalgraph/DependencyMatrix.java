package org.slizaa.server.graphql.hierarchicalgraph;


import java.util.ArrayList;
import java.util.List;

public class DependencyMatrix {

    private List<Node> orderedNodes;
    
    private int[][] matrix;

    public DependencyMatrix(List<Node> orderedNodes, int[][] matrix) {
        this.orderedNodes = orderedNodes;
        this.matrix = matrix;
    }
    
    public List<Node> orderedNodes() {
    	return orderedNodes;
    }

    public List<Cell> getCells() {
        List<Cell> result = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                result.add(new Cell(i, j, matrix[i][j]));
            }
        }
        return result;
    }
}

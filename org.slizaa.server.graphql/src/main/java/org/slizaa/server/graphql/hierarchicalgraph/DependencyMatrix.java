package org.slizaa.server.graphql.hierarchicalgraph;


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
    
    public int[][] matrix() {
    	return matrix;
    }
}

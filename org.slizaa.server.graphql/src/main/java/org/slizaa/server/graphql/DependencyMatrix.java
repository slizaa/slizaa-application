package org.slizaa.server.graphql;


import java.util.List;
import java.util.stream.Collectors;

public class DependencyMatrix {

    private List<Node> nodes;

    private List<List<Dependency>> dependencies;

    private List<List<Integer>> matrix;

    public DependencyMatrix(List<Node> nodes, List<List<Dependency>> dependencies) {
        this.nodes = nodes;
        this.dependencies = dependencies;

        matrix = dependencies.stream().map(row -> row.stream().map(dep -> dep.getWeight()).collect(Collectors.toList())).collect(Collectors.toList());
    }
}

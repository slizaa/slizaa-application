package org.slizaa.server.graphql;

import java.util.List;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;

public class Query implements GraphQLQueryResolver {

    public Node rootNode() {
        return new Node("12", "BUMM");
    }
}
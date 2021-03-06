package org.slizaa.server.graphql.graphdatabase;

import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;

import graphql.schema.DataFetchingEnvironment;

@Component
public class GraphDatabaseMutation extends AbstractDatabaseAwareComponent implements GraphQLMutationResolver {

  public GraphDatabase createGraphDatabase(String identifier, DataFetchingEnvironment environment) {
    
    return GraphDatabase.convert(slizaaService().newGraphDatabase(identifier));
  }

  public GraphDatabase startGraphDatabase(String databaseId, DataFetchingEnvironment environment) {

    return executeOnDatabase(environment, databaseId, database -> {
      database.start();
    });
  }

  public GraphDatabase stopGraphDatabase(String databaseId, DataFetchingEnvironment environment) {

    return executeOnDatabase(environment, databaseId, database -> {
      database.stop();
    });
  }

  public GraphDatabase terminateGraphDatabase(String databaseId, DataFetchingEnvironment environment) {

    return executeOnDatabase(environment, databaseId, database -> {
      database.terminate();
    });
  }
  
  public GraphDatabase parseGraphDatabase(String databaseId, DataFetchingEnvironment environment) {

    return executeOnDatabase(environment, databaseId, database -> {
      database.parse(true);
    });
  }

  public GraphDatabase setGraphDatabaseContentDefinition(String databaseId, String contentDefinitionFactoryId,
      String contentDefinition, DataFetchingEnvironment environment) {

    return executeOnDatabase(environment, databaseId, database -> {
      database.setContentDefinition(contentDefinitionFactoryId, contentDefinition);
    });
  }

  public GraphDatabase createHierarchicalGraph(String databaseId, String hierarchicalGraphId, DataFetchingEnvironment environment) {

    return executeOnDatabase(environment, databaseId, database -> {
      database.newHierarchicalGraph(hierarchicalGraphId);
    });
  }
  
  public GraphDatabase disposeHierarchicalGraph(String databaseId, String hierarchicalGraphId, DataFetchingEnvironment environment) {

    return executeOnDatabase(environment, databaseId, database -> {
      database.disposeHierarchicalGraph(hierarchicalGraphId);
    });
  }
}
package org.slizaa.server.graphql.graphdatabase;

import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;

@Component
public class GraphDatabaseMutation extends AbstractDatabaseAwareComponent implements GraphQLMutationResolver {

  public GraphDatabase newGraphDatabase(String identifier) {
    
    return GraphDatabase.convert(slizaaService().newGraphDatabase(identifier));
  }

  public GraphDatabase startGraphDatabase(String databaseId) {

    return executeOnDatabase(databaseId, database -> {
      database.start();
    });
  }

  public GraphDatabase stopGraphDatabase(String databaseId) {

    return executeOnDatabase(databaseId, database -> {
      database.stop();
    });
  }

  public GraphDatabase populateGraphDatabase(String databaseId) {

    return executeOnDatabase(databaseId, database -> {
      database.parse(true);
    });
  }

  public GraphDatabase setContentDefinition(String databaseId, String contentDefinitionFactoryId,
      String contentDefinition) {

    return executeOnDatabase(databaseId, database -> {
      database.setContentDefinition(contentDefinitionFactoryId, contentDefinition);
    });
  }

  public GraphDatabase createHierarchicalGraph(String databaseId, String hierarchicalGraphId) {

    return executeOnDatabase(databaseId, database -> {
      database.newHierarchicalGraph(hierarchicalGraphId);
    });
  }
}
package org.slizaa.server.graphql.graphdatabase;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slizaa.server.graphql.hierarchicalgraph.HierarchicalGraph;
import org.slizaa.server.service.slizaa.IGraphDatabase;
import org.slizaa.server.service.slizaa.IHierarchicalGraph;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;

/**
 *
 */
@Component
public class GraphDatabaseQuery extends AbstractDatabaseAwareComponent implements GraphQLQueryResolver {

  /**
   *
   * @return
   */
  public List<ContentDefinitionType> contentDefinitionFactories() {
    return slizaaService().getContentDefinitionProviderFactories().stream().map(factory -> new ContentDefinitionType(factory.getFactoryId(), factory.getName(), factory.getDescription()))
            .collect(Collectors.toList());
  }

  /**
   *
   * @return
   */
  public List<GraphDatabase> graphDatabases() {

    return slizaaService().getGraphDatabases().stream().map(db -> GraphDatabase.convert(db))
        .collect(Collectors.toList());
  }

  public GraphDatabase graphDatabase(String identifier) {

    IGraphDatabase graphDatabase = slizaaService().getGraphDatabase(identifier);
    return graphDatabase != null ? GraphDatabase.convert(graphDatabase) : null;
  }

  public HierarchicalGraph hierarchicalGraph(String databaseIdentifier, String hierarchicalGraphIdentifier) {

    IGraphDatabase graphDatabase = slizaaService().getGraphDatabase(databaseIdentifier);

    if (graphDatabase != null) {

      IHierarchicalGraph hierarchicalGraph = graphDatabase.getHierarchicalGraph(hierarchicalGraphIdentifier);

      if (hierarchicalGraph != null) {
        return new HierarchicalGraph(databaseIdentifier, hierarchicalGraphIdentifier);
      }
    }
    
    return null;
  }
}
package org.slizaa.server.graphql.graphdatabase;

import org.slizaa.server.graphql.SlizaaGraphQLError;
import org.slizaa.server.service.slizaa.IGraphDatabase;
import org.slizaa.server.service.slizaa.ISlizaaService;
import org.springframework.beans.factory.annotation.Autowired;

import graphql.schema.DataFetchingEnvironment;

public abstract class AbstractDatabaseAwareComponent {

  //
  @Autowired
  private ISlizaaService _slizaaService;

  /**
   * 
   * @return
   */
  protected ISlizaaService slizaaService() {
    return _slizaaService;
  }
  
  /**
   * 
   * @param databaseId
   * @param consumer
   * @return
   */
  protected GraphDatabase executeOnDatabase(DataFetchingEnvironment environment, String databaseId, DatabaseConsumer consumer) {
  
    // get the database
    IGraphDatabase database = _slizaaService.getGraphDatabase(databaseId);

    // check exists
    if (database == null) {
      // TODO:
    }

    //
    try {
      consumer.accept(database);
    } catch (Exception e) {
      environment.getExecutionContext().addError(new SlizaaGraphQLError(e.getMessage(), null, null));
    }

    // return the result
    return GraphDatabase.convert(database);
  }

  @FunctionalInterface
  public interface DatabaseConsumer {

    void accept(IGraphDatabase database) throws Exception;
  }
}

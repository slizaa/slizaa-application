package org.slizaa.server.service.slizaa.internal.graphdatabase;

import org.slizaa.server.service.slizaa.IGraphDatabase.GraphDatabaseAction;

public enum GraphDatabaseTrigger {

  //
  SET_CONTENT_DEFINITION(GraphDatabaseAction.SET_CONTENT_DEFINITION), 
  
  //
  PARSE(GraphDatabaseAction.PARSE), 
  
  //
  PARSING_COMPLETED(null), 
  
  //
  START(GraphDatabaseAction.START), 
  
  //
  STOP(GraphDatabaseAction.STOP), 
  
  //
  TERMINATE(GraphDatabaseAction.TERMINATE);

  public GraphDatabaseAction getAction() {
    return _action;
  }

  private GraphDatabaseTrigger(GraphDatabaseAction action) {
    _action = action;
  }

  private GraphDatabaseAction _action;
}

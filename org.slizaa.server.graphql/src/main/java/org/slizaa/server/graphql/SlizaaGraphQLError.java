package org.slizaa.server.graphql;

import java.util.List;
import java.util.Map;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

public class SlizaaGraphQLError implements GraphQLError {
  
  private String _message;

  private List<SourceLocation> _locations;
  
  private List<Object> _path;
  
  public SlizaaGraphQLError(String message, List<SourceLocation> locations, List<Object> path) {
    _message = message;
    _locations = locations;
    _path = path;
  }

  @Override
  public List<SourceLocation> getLocations() {
    return _locations;
  }

  @Override
  public ErrorType getErrorType() {
    return ErrorType.ValidationError;
  }

  @Override
  public String getMessage() {
    return _message;
  }

  @Override
  public List<Object> getPath() {
    return _path;
  }

  @Override
  public Map<String, Object> toSpecification() {
    return GraphQLError.super.toSpecification();
  }

  @Override
  public Map<String, Object> getExtensions() {
    return GraphQLError.super.getExtensions();
  }  
}

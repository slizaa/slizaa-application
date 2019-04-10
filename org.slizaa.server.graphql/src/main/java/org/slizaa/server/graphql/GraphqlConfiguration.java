package org.slizaa.server.graphql;

import graphql.ErrorType;
import graphql.ExceptionWhileDataFetching;
import graphql.ExecutionResult;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import graphql.servlet.GraphQLErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@ComponentScan(basePackageClasses = { GraphqlConfiguration.class })
public class GraphqlConfiguration {

  @Bean
  public GraphQLErrorHandler errorHandler() {
    return new GraphQLErrorHandler() {

      @Override
      public List<GraphQLError> processErrors(List<GraphQLError> errors) {
        return errors.stream().map(e -> new GraphQLErrorAdapter(e))
            .collect(Collectors.toList());
      }
    };
  }

  private class GraphQLErrorAdapter implements GraphQLError {

    private static final long serialVersionUID = 1308954323465652533L;

    private GraphQLError      error;

    public GraphQLErrorAdapter(GraphQLError error) {
      this.error = error;
    }

    @Override
    public Map<String, Object> getExtensions() {
      return error.getExtensions();
    }

    @Override
    public List<SourceLocation> getLocations() {
      return error.getLocations();
    }

    @Override
    public ErrorType getErrorType() {
      return error.getErrorType();
    }

    @Override
    public List<Object> getPath() {
      return error.getPath();
    }

    @Override
    public Map<String, Object> toSpecification() {
      Map<String, Object> result = error.toSpecification();
      result.remove("exception");
      return result;
    }

    @Override
    public String getMessage() {
      if (error instanceof ExceptionWhileDataFetching) {
        return ((ExceptionWhileDataFetching) error).getException().getMessage();
      }
      return error.getMessage();
    }
  }
}
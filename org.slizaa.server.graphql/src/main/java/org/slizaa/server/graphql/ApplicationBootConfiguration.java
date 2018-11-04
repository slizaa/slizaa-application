package org.slizaa.server.graphql;

import graphql.Scalars;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApplicationBootConfiguration {

  public static void main(String[] args) {
    SpringApplication.run(ApplicationBootConfiguration.class, args);
  }

  @Bean
  GraphQLSchema schema() {
    return GraphQLSchema.newSchema()
        .query(GraphQLObjectType.newObject()
            .name("query")
            .field(field -> field
                .name("test")
                .type(Scalars.GraphQLString)
                .dataFetcher(environment -> "response")
            )
            .build())
        .build();
  }
}
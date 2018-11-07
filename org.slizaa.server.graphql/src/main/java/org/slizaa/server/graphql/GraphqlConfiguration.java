package org.slizaa.server.graphql;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphqlConfiguration {

  @Bean
  public NodeResolver nodeResolver() {
    return new NodeResolver();
  }

  @Bean
  public Query query() {
    return new Query();
  }

//
//    @Bean
//    public Mutation mutation(PostDao postDao) {
//        return new Mutation(postDao);
//    }
}
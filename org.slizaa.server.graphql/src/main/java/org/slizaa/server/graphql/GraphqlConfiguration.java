package org.slizaa.server.graphql;

import org.slizaa.server.graphql.graphdatabase.MvnBasedContentDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.coxautodev.graphql.tools.SchemaParserDictionary;

@Configuration
@ComponentScan(basePackageClasses = { GraphqlConfiguration.class } )
public class GraphqlConfiguration {
  
  @Bean
  public SchemaParserDictionary schemaParserDictionary() {
    return new SchemaParserDictionary()
        .add(MvnBasedContentDefinition.class);
  }

}
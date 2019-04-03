package org.slizaa.server.graphql;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = { GraphqlConfiguration.class } )
public class GraphqlConfiguration {

}
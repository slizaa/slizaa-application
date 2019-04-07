package org.slizaa.server.service.slizaa;

import org.slizaa.hierarchicalgraph.graphdb.mapping.service.IMappingService;
import org.slizaa.server.service.slizaa.internal.SlizaaServiceDatabaseProperties;
import org.slizaa.server.service.slizaa.internal.SlizaaServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {SlizaaServiceImpl.class})
@EnableConfigurationProperties(SlizaaServiceDatabaseProperties.class)
public class SlizaaServiceModuleConfiguration {
  
  @Bean
  public IMappingService mappingService() {
    return IMappingService.createHierarchicalgraphMappingService();
  }
}

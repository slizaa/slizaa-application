package org.slizaa.server.service.slizaa;

import org.slizaa.server.service.slizaa.internal.SlizaaServiceImpl;
import org.slizaa.server.service.slizaa.internal.SlizaaServiceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {SlizaaServiceImpl.class})
@EnableConfigurationProperties(SlizaaServiceProperties.class)
public class SlizaaServiceModuleConfiguration {
}

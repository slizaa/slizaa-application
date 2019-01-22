package org.slizaa.server.service.configuration;

import org.slizaa.server.service.configuration.impl.ConfigurationServiceProperties;
import org.slizaa.server.service.configuration.impl.ConfigurationServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = { ConfigurationServiceImpl.class })
@EnableConfigurationProperties(ConfigurationServiceProperties.class)
public class ConfigurationModuleConfiguration {

}

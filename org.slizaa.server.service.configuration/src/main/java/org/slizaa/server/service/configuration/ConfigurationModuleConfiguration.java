package org.slizaa.server.service.configuration;

import org.slizaa.server.service.configuration.impl.ConfigurationServiceImpl;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = { ConfigurationServiceImpl.class })
public class ConfigurationModuleConfiguration {
}

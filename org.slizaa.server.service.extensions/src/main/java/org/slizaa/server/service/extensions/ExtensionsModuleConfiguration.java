package org.slizaa.server.service.extensions;

import org.slizaa.server.service.extensions.impl.DefaultExtensionService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = DefaultExtensionService.class)
public class ExtensionsModuleConfiguration {
}

package org.slizaa.server.extensions;

import org.slizaa.server.extensions.impl.DefaultExtensionService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = DefaultExtensionService.class)
public class ExtensionsConfiguration {
}

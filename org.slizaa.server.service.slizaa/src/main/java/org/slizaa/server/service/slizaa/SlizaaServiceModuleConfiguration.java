package org.slizaa.server.service.slizaa;

import org.slizaa.server.service.slizaa.internal.SlizaaComponent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = { SlizaaComponent.class })
public class SlizaaServiceModuleConfiguration {
}

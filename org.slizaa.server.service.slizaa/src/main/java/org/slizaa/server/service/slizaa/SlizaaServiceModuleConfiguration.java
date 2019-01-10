package org.slizaa.server.service.slizaa;

import org.slizaa.server.service.slizaa.internal.SlizaaServiceImpl;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = { SlizaaServiceImpl.class })
public class SlizaaServiceModuleConfiguration {
}

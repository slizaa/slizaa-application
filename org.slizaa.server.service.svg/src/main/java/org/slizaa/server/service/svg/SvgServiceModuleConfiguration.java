package org.slizaa.server.service.svg;

import org.slizaa.server.service.svg.impl.SvgServiceImpl;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {SvgServiceImpl.class})
public class SvgServiceModuleConfiguration {
}

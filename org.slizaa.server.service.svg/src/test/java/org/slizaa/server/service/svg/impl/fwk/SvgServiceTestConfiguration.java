package org.slizaa.server.service.svg.impl.fwk;

import org.slizaa.server.service.configuration.EnableConfigurationModule;
import org.slizaa.server.service.svg.impl.SvgServiceTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration()
@ComponentScan(basePackageClasses = SvgServiceTest.class)
@EnableConfigurationModule
public class SvgServiceTestConfiguration {


}

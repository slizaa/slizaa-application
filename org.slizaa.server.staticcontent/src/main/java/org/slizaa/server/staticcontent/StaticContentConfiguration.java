package org.slizaa.server.staticcontent;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = StaticContentController.class)
public class StaticContentConfiguration {
}

package org.slizaa.server.service.backend;

import org.slizaa.server.service.extensions.ExtensionsModuleConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE})
@Documented
@Import(BackendModuleConfiguration.class)
@Configuration
public @interface EnableBackendModule {
}
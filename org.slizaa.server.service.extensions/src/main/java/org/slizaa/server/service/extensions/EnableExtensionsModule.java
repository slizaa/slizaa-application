package org.slizaa.server.service.extensions;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE})
@Documented
@Import(ExtensionsModuleConfiguration.class)
@Configuration
public @interface EnableExtensionsModule {
}
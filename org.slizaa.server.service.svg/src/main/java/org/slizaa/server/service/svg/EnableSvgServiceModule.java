package org.slizaa.server.service.svg;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE})
@Documented
@Import(SvgServiceModuleConfiguration.class)
@Configuration
public @interface EnableSvgServiceModule {
}
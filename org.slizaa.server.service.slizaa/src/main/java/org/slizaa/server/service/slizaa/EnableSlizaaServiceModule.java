package org.slizaa.server.service.slizaa;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE})
@Documented
@Import(SlizaaServiceModuleConfiguration.class)
@Configuration
public @interface EnableSlizaaServiceModule {
}
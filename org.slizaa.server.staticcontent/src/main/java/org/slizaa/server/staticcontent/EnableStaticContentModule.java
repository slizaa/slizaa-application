package org.slizaa.server.staticcontent;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE})
@Documented
@Import(StaticContentConfiguration.class)
@Configuration
public @interface EnableStaticContentModule {
}
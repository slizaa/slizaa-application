package org.slizaa.server.graphql;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE})
@Documented
@Import(GraphqlConfiguration.class)
@Configuration
public @interface EnableGraphqlModule {
}
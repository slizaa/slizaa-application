package org.slizaa.server;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slizaa.server.fwk.GraphQLResponse;
import org.slizaa.server.fwk.GraphQLTestTemplate;
import org.slizaa.server.graphql.EnableGraphqlModule;
import org.slizaa.server.service.backend.EnableBackendModule;
import org.slizaa.server.service.extensions.EnableExtensionsModule;
import org.slizaa.server.service.slizaa.EnableSlizaaServiceModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@GraphQLTest
@EnableExtensionsModule
@EnableBackendModule
@EnableSlizaaServiceModule
@EnableGraphqlModule
public class GraphQLToolsSampleApplicationTest {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void installExtensionsAndCreateDatabase() throws IOException {

        //
        GraphQLResponse response = assertOk(graphQLTestTemplate.perform("graphql/isBackendConfigured.graphql", null));
        assertThat(response.get("$.data.isBackendConfigured", Boolean.class)).isFalse();

        //
        response = assertOk(graphQLTestTemplate.perform("graphql/availableServerExtensions.graphql", null));
        assertThat(response.get("$.data.availableServerExtensions[0].symbolicName")).isEqualTo("org.slizaa.neo4j.backend");
        assertThat(response.get("$.data.availableServerExtensions[0].version")).isEqualTo("1.0.0");
        assertThat(response.get("$.data.availableServerExtensions[1].symbolicName")).isEqualTo("org.slizaa.jtype.extension");
        assertThat(response.get("$.data.availableServerExtensions[1].version")).isEqualTo("1.0.0");

        //
        response = assertOk(graphQLTestTemplate.perform("graphql/installServerExtensions.graphql", null));
        assertThat(response.get("$.data.installServerExtensions[0].symbolicName")).isEqualTo("org.slizaa.neo4j.backend");
        assertThat(response.get("$.data.installServerExtensions[0].version")).isEqualTo("1.0.0");
        assertThat(response.get("$.data.installServerExtensions[1].symbolicName")).isEqualTo("org.slizaa.jtype.extension");
        assertThat(response.get("$.data.installServerExtensions[1].version")).isEqualTo("1.0.0");

        //
        ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("identifier", "lorem-ipsum-dolor-sit-amet");
        response = assertOk(graphQLTestTemplate.perform("graphql/newStructureDatabase.graphql", variables));
        assertThat(response.get("$.data.newStructureDatabase.identifier")).isEqualTo("lorem-ipsum-dolor-sit-amet");

        //
        response = assertOk(graphQLTestTemplate.perform("graphql/structureDatabases.graphql", null));
        assertThat(response.get("$.data.structureDatabases[0].identifier")).isEqualTo("lorem-ipsum-dolor-sit-amet");
    }

    /**
     *
     * @param graphQLResponse
     * @return
     */
    private GraphQLResponse assertOk(GraphQLResponse graphQLResponse) {
        assertThat(graphQLResponse).isNotNull();
        assertThat(graphQLResponse.isOk()).isTrue();
        return graphQLResponse;
    }
}
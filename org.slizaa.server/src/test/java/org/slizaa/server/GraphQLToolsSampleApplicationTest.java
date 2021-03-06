package org.slizaa.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slizaa.server.fwk.GraphQLResponse;
import org.slizaa.server.fwk.GraphQLTestTemplate;
import org.slizaa.server.graphql.EnableGraphqlModule;
import org.slizaa.server.service.backend.EnableBackendServiceModule;
import org.slizaa.server.service.configuration.EnableConfigurationModule;
import org.slizaa.server.service.extensions.EnableExtensionsModule;
import org.slizaa.server.service.slizaa.EnableSlizaaServiceModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@GraphQLTest
@EnableExtensionsModule
@EnableBackendServiceModule
@EnableSlizaaServiceModule
@EnableGraphqlModule
@EnableConfigurationModule
public class GraphQLToolsSampleApplicationTest {

	public static final String DATABASE_NAME = "lorem-ipsum-dolor-sit-amet";

	public static final String MAPPED_SYSTEM_NAME = "hurz";

	@Autowired
	private GraphQLTestTemplate graphQLTestTemplate;

	@Test
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
	public void installExtensionsAndCreateDatabase() throws IOException {

		//
		GraphQLResponse response = assertOk(
				graphQLTestTemplate.perform("/graphql/hasInstalledExtensions.graphql", null));

		if (!response.get("$.data.hasInstalledExtensions", Boolean.class)) {

			//
			response = assertOk(graphQLTestTemplate.perform("/graphql/availableServerExtensions.graphql", null));
			assertThat(response.get("$.data.availableServerExtensions[0].symbolicName"))
					.isEqualTo("org.slizaa.neo4j.backend");
			assertThat(response.get("$.data.availableServerExtensions[0].version")).isEqualTo("1.0.0");
			assertThat(response.get("$.data.availableServerExtensions[1].symbolicName"))
					.isEqualTo("org.slizaa.jtype.extension");
			assertThat(response.get("$.data.availableServerExtensions[1].version")).isEqualTo("1.0.0");

			//
			response = assertOk(graphQLTestTemplate.perform("/graphql/installServerExtensions.graphql", null));
			assertThat(response.get("$.data.installServerExtensions[0].symbolicName"))
					.isEqualTo("org.slizaa.neo4j.backend");
			assertThat(response.get("$.data.installServerExtensions[0].version")).isEqualTo("1.0.0");
			assertThat(response.get("$.data.installServerExtensions[1].symbolicName"))
					.isEqualTo("org.slizaa.jtype.extension");
			assertThat(response.get("$.data.installServerExtensions[1].version")).isEqualTo("1.0.0");
		}

		//
		ObjectNode variables = new ObjectMapper().createObjectNode();
		variables.put("identifier", DATABASE_NAME);
		response = assertOk(graphQLTestTemplate.perform("/graphql/newStructureDatabase.graphql", variables));
		assertThat(response.get("$.data.newGraphDatabase.identifier")).isEqualTo(DATABASE_NAME);

		//
		response = assertOk(graphQLTestTemplate.perform("/graphql/structureDatabases.graphql", null));
		assertThat(response.get("$.data.graphDatabases[0].identifier")).isEqualTo(DATABASE_NAME);

		//
		variables = new ObjectMapper().createObjectNode();
		variables.put("identifier", DATABASE_NAME);
		variables.put("coords", "org.springframework:spring-core:5.1.3.RELEASE");
		response = assertOk(graphQLTestTemplate.perform("/graphql/setMvnBasedContentDefinition.graphql", variables));
		assertThat(response.get("$.data.setMvnBasedContentDefinition[0].artifactId")).isEqualTo("spring-core");

		//
		variables = new ObjectMapper().createObjectNode();
		variables.put("identifier", DATABASE_NAME);
		response = assertOk(graphQLTestTemplate.perform("/graphql/parseContent.graphql", variables));
		assertThat(response.get("$.data.parseContent.identifier")).isEqualTo(DATABASE_NAME);

		//
		variables = new ObjectMapper().createObjectNode();
		variables.put("databaseId", DATABASE_NAME);
		variables.put("mappedSystemId", MAPPED_SYSTEM_NAME);
		response = assertOk(graphQLTestTemplate.perform("/graphql/mapSystem.graphql", variables));
		System.out.println(response.getRawResponse().getBody());
		assertThat(response.get("$.data.mapSystem.identifier")).isEqualTo(DATABASE_NAME);
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
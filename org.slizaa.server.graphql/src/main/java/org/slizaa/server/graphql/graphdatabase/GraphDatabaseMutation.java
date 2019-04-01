package org.slizaa.server.graphql.graphdatabase;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.slizaa.core.mvnresolver.api.IMvnCoordinate;
import org.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProvider;
import org.slizaa.server.service.slizaa.ISlizaaService;
import org.slizaa.server.service.slizaa.IGraphDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GraphDatabaseMutation implements GraphQLMutationResolver {

	//
	@Autowired
	private ISlizaaService slizaaService;

	/**
	 * @param identifier
	 * @return
	 */
	public GraphDatabase newGraphDatabase(String identifier) {

		// create the structure database
		IGraphDatabase structureDatabase = slizaaService.newGraphDatabase(identifier);

		// return the result
		return GraphDatabase.convert(structureDatabase);
	}

	public GraphDatabase startGraphDatabase(String identifier) {

		// get the structure database
		// TODO: check exists
		IGraphDatabase structureDatabase = slizaaService.getGraphDatabase(identifier);

		try {
			structureDatabase.start();
		} catch (IllegalStateException exception) {
			// TODO: log
		}

		// return the result
		return GraphDatabase.convert(structureDatabase);
	}

	public GraphDatabase stopGraphDatabase(String identifier) {

		// get the structure database
		// TODO: check exists
		IGraphDatabase structureDatabase = slizaaService.getGraphDatabase(identifier);

		try {
			structureDatabase.stop();
		} catch (IllegalStateException exception) {
			// TODO: log
		}

		// return the result
		return GraphDatabase.convert(structureDatabase);
	}

	public GraphDatabase populateGraphDatabase(String identifier) {

		IGraphDatabase structureDatabase = slizaaService.getGraphDatabase(identifier);

		//
		try {
			structureDatabase.parse(true);
		} catch (IllegalStateException exception) {
			// TODO: log
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// return the result
		return GraphDatabase.convert(structureDatabase);
	}

	public MvnBasedContentDefinition setMvnBasedContentDefinition(String identifier, List<String> artifactIDs) {

		//
		MvnBasedContentDefinitionProvider mvnBasedContentDefinitionProvider = new MvnBasedContentDefinitionProvider();

		List<MvnCoordinate> result = new ArrayList<>();

		//
		for (String artifactID : artifactIDs) {
			IMvnCoordinate mvnCoordinate = mvnBasedContentDefinitionProvider.addArtifact(artifactID);
			result.add(new MvnCoordinate(mvnCoordinate));
		}

		//
		IGraphDatabase structureDatabase = slizaaService.getGraphDatabase(identifier);
		structureDatabase.setContentDefinitionProvider(mvnBasedContentDefinitionProvider);

		//
		return new MvnBasedContentDefinition(result);
	}

	public GraphDatabase createHierarchicalGraph(String databaseId, String hierarchicalGraphId) {

		IGraphDatabase structureDatabase = slizaaService.getGraphDatabase(databaseId);

		//
		try {
			structureDatabase.createNewHierarchicalGraph(hierarchicalGraphId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// return the result
		return GraphDatabase.convert(structureDatabase);
	}
}
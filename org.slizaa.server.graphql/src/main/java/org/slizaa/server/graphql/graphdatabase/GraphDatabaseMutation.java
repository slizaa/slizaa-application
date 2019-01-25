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
		return new GraphDatabase(structureDatabase.getIdentifier());
	}

	public GraphDatabase startGraphDatabase(String identifier) {

		// get the structure database
		// TODO: check exists
		IGraphDatabase structureDatabase = slizaaService.getGraphDatabase(identifier);
		
		// TODO: check state
		structureDatabase.start();

		// return the result
		return new GraphDatabase(structureDatabase.getIdentifier());
	}

	public GraphDatabase stopGraphDatabase(String identifier) {

		// get the structure database
		// TODO: check exists
		IGraphDatabase structureDatabase = slizaaService.getGraphDatabase(identifier);
		
		// TODO: check state
		structureDatabase.stop();

		// return the result
		return new GraphDatabase(structureDatabase.getIdentifier());
	}
	
	public GraphDatabase populateGraphDatabase(String identifier) {

		IGraphDatabase structureDatabase = slizaaService.getGraphDatabase(identifier);

		//
		try {
			structureDatabase.parse(true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// return the result
		return new GraphDatabase(structureDatabase.getIdentifier());
	}

	public List<MvnCoordinate> setMvnBasedContentDefinition(String identifier, List<String> artifactIDs) {

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
		return result;
	}

	public GraphDatabase mapSystem(String databaseId, String mappedSystemId) {

		IGraphDatabase structureDatabase = slizaaService.getGraphDatabase(databaseId);

		//
		try {
			structureDatabase.createNewHierarchicalGraph(mappedSystemId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// return the result
		return new GraphDatabase(structureDatabase.getIdentifier());
	}
}
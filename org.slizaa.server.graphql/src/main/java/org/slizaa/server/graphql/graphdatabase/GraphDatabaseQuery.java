package org.slizaa.server.graphql.graphdatabase;

import java.util.List;
import java.util.stream.Collectors;

import org.slizaa.server.service.slizaa.ISlizaaService;
import org.slizaa.server.service.slizaa.IGraphDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;

/**
 *
 */
@Component
public class GraphDatabaseQuery implements GraphQLQueryResolver {

	//
	@Autowired
	private ISlizaaService slizaaService;

	/**
	 *
	 * @return
	 */
	public List<GraphDatabase> graphDatabases() {
		return slizaaService.getGraphDatabases().stream()
				.map(db -> new GraphDatabase(((IGraphDatabase) db).getIdentifier()))
				.collect(Collectors.toList());
	}

	public GraphDatabase graphDatabase(String identifier) {
		IGraphDatabase structureDatabase = slizaaService.getGraphDatabase(identifier);
		return structureDatabase != null ? new GraphDatabase(structureDatabase.getIdentifier()) : null;
	}
}
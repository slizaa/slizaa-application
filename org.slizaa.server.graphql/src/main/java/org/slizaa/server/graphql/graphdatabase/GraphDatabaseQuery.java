package org.slizaa.server.graphql.graphdatabase;

import java.util.List;
import java.util.stream.Collectors;

import org.slizaa.server.service.slizaa.ISlizaaService;
import org.slizaa.server.graphql.hierarchicalgraph.HierarchicalGraph;
import org.slizaa.server.service.slizaa.IGraphDatabase;
import org.slizaa.server.service.slizaa.IHierarchicalGraph;
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
				.map(db -> GraphDatabase.convert(db))
				.collect(Collectors.toList());
	}

	public GraphDatabase graphDatabase(String identifier) {
		IGraphDatabase graphDatabase = slizaaService.getGraphDatabase(identifier);
		return graphDatabase != null ? GraphDatabase.convert(graphDatabase) : null;
	}
	
	public HierarchicalGraph hierarchicalGraph(String databaseIdentifier, String hierarchicalGraphIdentifier) {
		IGraphDatabase graphDatabase = slizaaService.getGraphDatabase(databaseIdentifier);
		if (graphDatabase != null) {
			IHierarchicalGraph hg = graphDatabase.getHierarchicalGraph(hierarchicalGraphIdentifier);
			if (hg != null) {
				return new HierarchicalGraph(databaseIdentifier, hierarchicalGraphIdentifier);
			}
		}
		return null;
	}
}
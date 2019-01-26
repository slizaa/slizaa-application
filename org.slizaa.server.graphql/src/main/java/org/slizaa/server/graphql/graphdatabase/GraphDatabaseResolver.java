package org.slizaa.server.graphql.graphdatabase;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slizaa.server.graphql.hierarchicalgraph.HierarchicalGraph;
import org.slizaa.server.service.slizaa.IGraphDatabase;
import org.slizaa.server.service.slizaa.IHierarchicalGraph;
import org.slizaa.server.service.slizaa.ISlizaaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLResolver;

/**
 * 
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
@Component
public class GraphDatabaseResolver implements GraphQLResolver<GraphDatabase> {

	@Autowired
	private ISlizaaService slizaaService;

	public List<HierarchicalGraph> getHierarchicalGraphs(GraphDatabase graphDatabase) {
		
		IGraphDatabase db = slizaaService.getGraphDatabase(graphDatabase.getIdentifier());
		
		if (db != null) {
			return db.getHierarchicalGraphs().stream()
					.map(hg -> new HierarchicalGraph(graphDatabase.getIdentifier(), hg.getIdentifier()))
					.collect(Collectors.toList());
		}
		
		return Collections.emptyList();
	}

	public HierarchicalGraph hierarchicalGraph(GraphDatabase graphDatabase, String identifier) {
		
		IGraphDatabase db = slizaaService.getGraphDatabase(graphDatabase.getIdentifier());
		
		if (db != null) {
			IHierarchicalGraph hg = db.getHierarchicalGraph(identifier);
			if (hg != null) {
				return new HierarchicalGraph(graphDatabase.getIdentifier(), identifier);
			}
		}
		
		return null;
	}
}

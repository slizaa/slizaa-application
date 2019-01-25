package org.slizaa.server.graphql.graphdatabase;

import java.util.Collections;
import java.util.List;

import org.slizaa.server.graphql.hierarchicalgraph.HierarchicalGraph;
import org.slizaa.server.service.slizaa.ISlizaaService;
import org.springframework.beans.factory.annotation.Autowired;

import com.coxautodev.graphql.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

@Component
public class GraphDatabaseResolver implements GraphQLResolver<GraphDatabase> {

	@Autowired
	private ISlizaaService slizaaService;
	
	 public List<HierarchicalGraph> getHierarchicalGraphs(GraphDatabase graphDatabase) {
//		 IGraphDatabase strDb = slizaaService.getGraphDatabase(graphDatabase.getIdentifier());
//		 strDb.getHierarchicalGraphs().stream().map(hg -> new HierarchicalGraph(graphDatabase.getIdentifier(), hg.getIdentifier()));
		 return Collections.emptyList();
	 }
}

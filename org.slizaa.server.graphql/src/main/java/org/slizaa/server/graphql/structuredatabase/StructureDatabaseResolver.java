package org.slizaa.server.graphql.structuredatabase;

import java.util.Collections;
import java.util.List;

import org.slizaa.server.service.slizaa.ISlizaaService;
import org.slizaa.server.service.slizaa.IStructureDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLResolver;

@Component
public class StructureDatabaseResolver implements GraphQLResolver<StructureDatabase> {

	@Autowired
	private ISlizaaService slizaaService;
	
	 public List<HierarchicalGraph> getHierarchicalGraphs(StructureDatabase structureDatabase) {
		 
		 IStructureDatabase strDb = slizaaService.getStructureDatabase(structureDatabase.getIdentifier());
		 strDb.getHierarchicalGraphs().stream().map(hg -> new HierarchicalGraph(structureDatabase.getIdentifier(), hg.getIdentifier()));
		 return Collections.emptyList();
	 }
	 
	 public HierarchicalGraph getHierarchicalGraph(StructureDatabase structureDatabase, String identifier) {
		 return null;
	 }
}

package org.slizaa.server.graphql.structuredatabase;

import java.util.List;
import java.util.stream.Collectors;

import org.slizaa.server.service.slizaa.ISlizaaService;
import org.slizaa.server.service.slizaa.IStructureDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;

/**
 *
 */
@Component
public class StructureDatabaseQuery implements GraphQLQueryResolver {

	//
	@Autowired
	private ISlizaaService slizaaService;

	/**
	 *
	 * @return
	 */
	public List<StructureDatabase> structureDatabases() {
		return slizaaService.getStructureDatabases().stream()
				.map(db -> new StructureDatabase(((IStructureDatabase) db).getIdentifier()))
				.collect(Collectors.toList());
	}

	public StructureDatabase structureDatabase(String identifier) {
		IStructureDatabase structureDatabase = slizaaService.getStructureDatabase(identifier);
		return structureDatabase != null ? new StructureDatabase(structureDatabase.getIdentifier()) : null;
	}
}
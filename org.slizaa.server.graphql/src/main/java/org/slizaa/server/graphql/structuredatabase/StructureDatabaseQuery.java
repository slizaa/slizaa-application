package org.slizaa.server.graphql.structuredatabase;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.slizaa.server.graphql.serverconfig.ServerExtension;
import org.slizaa.server.service.slizaa.ISlizaaService;
import org.slizaa.server.service.slizaa.IStructureDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    return slizaaService.getStructureDatabases().stream().map(db -> new StructureDatabase(((IStructureDatabase) db).getIdentifier())).collect(Collectors.toList());
  }
}
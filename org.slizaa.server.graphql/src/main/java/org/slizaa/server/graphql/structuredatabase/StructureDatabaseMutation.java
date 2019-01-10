package org.slizaa.server.graphql.structuredatabase;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.slizaa.core.mvnresolver.api.IMvnCoordinate;
import org.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProvider;
import org.slizaa.server.service.slizaa.ISlizaaService;
import org.slizaa.server.service.slizaa.IStructureDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class StructureDatabaseMutation implements GraphQLMutationResolver {

  //
  @Autowired
  private ISlizaaService slizaaService;

  /**
   * @param identifier
   * @return
   */
  public StructureDatabase newStructureDatabase(String identifier) {

    // create the structure database
    IStructureDatabase structureDatabase = slizaaService.newStructureDatabase(identifier);

    // return the result
    return new StructureDatabase(structureDatabase.getIdentifier());
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

    // TODO: new vs get
    IStructureDatabase structureDatabase = slizaaService.newStructureDatabase(identifier);
    structureDatabase.setContentDefinitionProvider(mvnBasedContentDefinitionProvider);

    //
    return result;
  }

  public StructureDatabase parseContent(String identifier) {

    // create the structure database
    // TODO
    IStructureDatabase structureDatabase = slizaaService.newStructureDatabase(identifier);

    //
    try {
      structureDatabase.parseAndStartDatabase();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // return the result
    return new StructureDatabase(structureDatabase.getIdentifier());
  }

  public StructureDatabase mapSystem(String identifier) {

    // create the structure database
    // TODO
    IStructureDatabase structureDatabase = slizaaService.newStructureDatabase(identifier);

    //
    try {
      structureDatabase.mapSystem();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    // return the result
    return new StructureDatabase(structureDatabase.getIdentifier());
  }
}
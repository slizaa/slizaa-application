package org.slizaa.server.graphql.structuredatabase;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProvider;
import org.slizaa.server.service.slizaa.ISlizaaService;
import org.slizaa.server.service.slizaa.IStructureDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public List<String> setMvnBasedContentDefinition(String identifier, List<String> artifactIDs) {

        //
        MvnBasedContentDefinitionProvider mvnBasedContentDefinitionProvider = new MvnBasedContentDefinitionProvider();

        //
        for (String artifactID : artifactIDs) {
            String[] coords = split(artifactID);
            mvnBasedContentDefinitionProvider.addArtifact(coords[0], coords[1], coords[2]);
        }

        // TODO
        IStructureDatabase structureDatabase = slizaaService.newStructureDatabase(identifier);
        structureDatabase.addContentDefinitionProvider(mvnBasedContentDefinitionProvider);

        //
        return artifactIDs;
    }

    private String[] split(String coordinate) {

        //
        String[] result = new String[3];

        String[] splitted = coordinate.split(":");
        result[0] = splitted[0];
        result[1] = splitted[1];
        if (splitted.length == 5) {
            result[1] = splitted[3];
        }
        if (splitted.length == 6) {
            result[1] = splitted[4];
        }
        return result;
    }
}
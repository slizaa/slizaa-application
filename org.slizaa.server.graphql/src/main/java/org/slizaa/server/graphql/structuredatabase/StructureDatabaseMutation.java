package org.slizaa.server.graphql.structuredatabase;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.slizaa.server.graphql.serverconfig.ServerExtension;
import org.slizaa.server.service.extensions.ExtensionIdentifier;
import org.slizaa.server.service.extensions.IExtension;
import org.slizaa.server.service.extensions.IExtensionIdentifier;
import org.slizaa.server.service.slizaa.ISlizaaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StructureDatabaseMutation implements GraphQLMutationResolver {

    //
    @Autowired
    private ISlizaaService slizaaService;

    /**
     *
     * @param identifier
     * @return
     */
    public StructureDatabase newStructureDatabase(String identifier) {

        slizaaService.newStructureDatabase(identifier);

        return new StructureDatabase(identifier);
    }
}
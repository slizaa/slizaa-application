package org.slizaa.server.graphql.serverconfig;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.slizaa.server.service.extensions.ExtensionIdentifier;
import org.slizaa.server.service.extensions.IExtension;
import org.slizaa.server.service.extensions.IExtensionIdentifier;
import org.slizaa.server.service.slizaa.ISlizaaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ServerConfigMutation implements GraphQLMutationResolver {

    @Autowired
    private ISlizaaService slizaaService;

    /**
     * <p></p>
     *
     * @return
     */
    public List<ServerExtension> installServerExtensions(List<ServerExtension> serverExtensions) {

        //
        List<IExtensionIdentifier> extensionIds = serverExtensions
            .stream().map(ext -> new ExtensionIdentifier(ext.getSymbolicName(), ext.getVersion()))
                .collect(Collectors.toList());

        //
        List<IExtension> extensions = slizaaService.getExtensionService().getExtensions(extensionIds);
        slizaaService.getBackendService().installExtensions(extensions);

        //
        return extensions.stream().map(ext -> new ServerExtension(ext.getSymbolicName(), ext.getVersion().toString())).collect(Collectors.toList());
    }
}
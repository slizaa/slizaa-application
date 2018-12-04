package org.slizaa.server.graphql;

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
public class Mutation implements GraphQLMutationResolver {

    //
    @Autowired
    private ISlizaaService slizaaService;

    /**
     * <p></p>
     *
     * @return
     */
    public List<BackendExtension> installBackendExtensions(List<BackendExtension> backendExtensions) {

        //
        List<IExtensionIdentifier> extensionIds = backendExtensions.stream().map(ext -> new ExtensionIdentifier(ext.getIdentifier(), ext.getVersion()))
                .collect(Collectors.toList());

        //
        List<IExtension> extensions =  slizaaService.installExtensions(extensionIds);

        //
        return extensions.stream().map(ext -> new BackendExtension(ext.getSymbolicName(), ext.getVersion().toString())).collect(Collectors.toList());
    }

    public List<BackendExtension> uninstallBackendExtensions(List<BackendExtension> backendExtensions) {

        //
        List<IExtensionIdentifier> extensionIds = backendExtensions.stream().map(ext -> new ExtensionIdentifier(ext.getIdentifier(), ext.getVersion()))
                .collect(Collectors.toList());

        //
        List<IExtension> extensions =  slizaaService.uninstallExtensions(extensionIds);

        //
        return extensions.stream().map(ext -> new BackendExtension(ext.getSymbolicName(), ext.getVersion().toString())).collect(Collectors.toList());
    }

//    public Post writePost(String title, String text, String category, String author) {
//        Post post = new Post();
//        post.setId(UUID.randomUUID().toString());
//        post.setTitle(title);
//        post.setText(text);
//        post.setCategory(category);
//        post.setAuthorId(author);
//        postDao.savePost(post);
//
//        return post;
//    }
}
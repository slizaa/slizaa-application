package org.slizaa.server.graphql;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.slizaa.server.service.extensions.DefaultExtensionIdentifier;
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
   * @return
   */
  public List<BackendExtension> installBackendExtensions(List<BackendExtension> backendExtensions) {

    List<BackendExtension>

    slizaaService.getExtensionService().getExtensions(
        backendExtensions.stream().map(ext -> new DefaultExtensionIdentifier(ext.getIdentifier(), ext.getVersion()))
            .collect(
                Collectors.toList()));
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
package org.slizaa.server.graphql;

import java.util.UUID;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

@Component
public class Mutation implements GraphQLMutationResolver {

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
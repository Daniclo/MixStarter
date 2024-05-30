package org.daniclo.mixstarter.data;

import org.daniclo.mixstarter.model.Post;

import java.util.List;

public interface PostDAO extends GenericDAO<Post> {
    List<Post> getPosts();
    List<Post> getPostsByUser(String username);
    List<Post> getPostsByTitle(String title);
}

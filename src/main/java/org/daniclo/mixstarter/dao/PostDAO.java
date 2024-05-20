package org.daniclo.mixstarter.dao;

import org.daniclo.mixstarter.model.Post;

import java.util.List;

public interface PostDAO extends GenericDAO<Post> {
    List<Post> getPosts();
}

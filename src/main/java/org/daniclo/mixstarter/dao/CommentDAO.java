package org.daniclo.mixstarter.dao;

import org.daniclo.mixstarter.model.Comment;

import java.util.List;

public interface CommentDAO extends GenericDAO<Comment> {
    List<Comment> getComments();
}

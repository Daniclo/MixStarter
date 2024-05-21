package org.daniclo.mixstarter.dao;

import org.daniclo.mixstarter.model.User;

import java.util.List;

public interface UserDAO extends GenericDAO<User> {
    List<User> getUsers();
    User getByName(String name);
    List<User> getFollowers(Long id);
    List<User> getFollowing(Long id);
}

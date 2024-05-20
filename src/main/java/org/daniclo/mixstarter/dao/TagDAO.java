package org.daniclo.mixstarter.dao;

import org.daniclo.mixstarter.model.Tag;

import java.util.List;

public interface TagDAO extends GenericDAO<Tag> {
    List<Tag> getTags();
}

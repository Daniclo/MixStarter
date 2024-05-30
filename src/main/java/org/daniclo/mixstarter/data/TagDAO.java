package org.daniclo.mixstarter.data;

import org.daniclo.mixstarter.model.Tag;

import java.util.List;

public interface TagDAO extends GenericDAO<Tag> {
    List<Tag> getTags();

    Tag getTagByName(String name);
}

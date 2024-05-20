package org.daniclo.mixstarter.dao;

import org.daniclo.mixstarter.model.Song;

import java.util.List;

public interface SongDAO extends GenericDAO<Song> {
    List<Song> getSongs();
}

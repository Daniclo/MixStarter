package org.daniclo.mixstarter.data;

import org.daniclo.mixstarter.model.Song;
import org.daniclo.mixstarter.model.User;

import java.util.List;

public interface SongDAO extends GenericDAO<Song> {
    List<Song> getSongs();
    List<Song> getSongsLikedByUser(User user);
    List<Song> getSongsByTag(String tag);
}

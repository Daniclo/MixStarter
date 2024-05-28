package org.daniclo.mixstarter.dao;

import org.daniclo.mixstarter.model.Album;
import org.daniclo.mixstarter.model.User;

import java.util.List;

public interface AlbumDAO extends GenericDAO<Album> {
    List<Album> getAlbums();
    List<Album> getAlbumsLikedByUser(User user);
    List<Album> getAlbumsByTag(String tag);
}

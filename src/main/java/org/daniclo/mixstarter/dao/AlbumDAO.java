package org.daniclo.mixstarter.dao;

import org.daniclo.mixstarter.model.Album;

import java.util.List;

public interface AlbumDAO extends GenericDAO<Album> {
    List<Album> getAlbums();
}

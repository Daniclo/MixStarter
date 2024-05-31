package org.daniclo.mixstarter.data;

import org.daniclo.mixstarter.model.Album;
import org.daniclo.mixstarter.model.User;
import org.daniclo.mixstarter.model.UserLikesAlbum;

public interface UserLikesAlbumDAO extends GenericDAO<UserLikesAlbum> {

    UserLikesAlbum findByUserAndAlbum(User user, Album album);

}
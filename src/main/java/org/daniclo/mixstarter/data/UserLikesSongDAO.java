package org.daniclo.mixstarter.data;

import org.daniclo.mixstarter.model.User;
import org.daniclo.mixstarter.model.Song;
import org.daniclo.mixstarter.model.UserLikesSong;

public interface UserLikesSongDAO extends GenericDAO<UserLikesSong>{

    UserLikesSong findByUserAndSong(User user, Song song);
}

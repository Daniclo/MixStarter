package org.daniclo.mixstarter.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "UsuarioLikesCancion")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserLikesSong {

    @EmbeddedId
    private UserLikesPK userLikesPK;

    @ManyToOne
    @ToString.Exclude
    @MapsId("userKey")
    @JoinColumn(name = "Usuario_idUsuario")
    private User user;

    @ManyToOne
    @ToString.Exclude
    @MapsId("likedKey")
    @JoinColumn(name = "Cancion_idCancion")
    private Song song;

    public UserLikesSong(User user, Song song){
        this.user = user;
        this.song = song;
        this.userLikesPK = new UserLikesPK(user.getId(), song.getId());
    }
}

package org.daniclo.mixstarter.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "UsuarioLikesAlbum")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserLikesAlbum {

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
    @JoinColumn(name = "Album_idAlbum")
    private Album album;

    public UserLikesAlbum(User user, Album album) {
        this.user = user;
        this.album = album;
        this.userLikesPK = new UserLikesPK(user.getId(), album.getId());
    }
}
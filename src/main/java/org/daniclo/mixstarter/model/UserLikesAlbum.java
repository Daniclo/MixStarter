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
@NoArgsConstructor
@ToString
public class UserLikesAlbum {

    @EmbeddedId
    private UserLikesPK userLikesPK = new UserLikesPK();

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @ToString.Exclude
    @MapsId("userKey")
    @JoinColumn(name = "Usuario_idUsuario")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @ToString.Exclude
    @MapsId("likedKey")
    @JoinColumn(name = "Album_idAlbum")
    private Album album;
}

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
    private UserLikesPK userLikesPK = new UserLikesPK();

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @ToString.Exclude
    @MapsId("userKey")
    @JoinColumn(name = "Usuario_idUsuario")
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @ToString.Exclude
    @MapsId("likedKey")
    @JoinColumn(name = "Cancion_idCancion")
    private Song song;
}

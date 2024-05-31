package org.daniclo.mixstarter.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "Seguidores")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Followers {

    @EmbeddedId
    private FollowersPK followersPK;

    @Column(name = "fechaSeguimiento", nullable = false)
    @CreationTimestamp
    private Timestamp followingDate;

    @ManyToOne
    @ToString.Exclude
    @MapsId("userFollows")
    @JoinColumn(name = "idSeguidor")
    private User follows;

    @ManyToOne
    @ToString.Exclude
    @MapsId("userFollowed")
    @JoinColumn(name = "idSeguido")
    private User followed;

    public Followers(User follows, User followed) {
        this.follows = follows;
        this.followed = followed;
        this.followersPK = new FollowersPK(follows.getId(), followed.getId());
    }
}

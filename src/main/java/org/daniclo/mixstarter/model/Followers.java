package org.daniclo.mixstarter.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "followers", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<User> followers = new ArrayList<>();

    @OneToMany(mappedBy = "followers", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<User> followed = new ArrayList<>();

}

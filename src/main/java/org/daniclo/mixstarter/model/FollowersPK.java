package org.daniclo.mixstarter.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Embeddable
public class FollowersPK implements Serializable {
    @Column(name = "idSeguidor")
    private long userFollows;

    @Column(name = "idSeguido")
    private long userFollowed;
}

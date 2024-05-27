package org.daniclo.mixstarter.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class UserLikesPK implements Serializable {
    private long userKey;
    private long likedKey;
}

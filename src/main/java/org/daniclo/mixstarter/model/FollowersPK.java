package org.daniclo.mixstarter.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Embeddable
public class FollowersPK implements Serializable {
    private long userFollows;
    private long userFollowed;
}

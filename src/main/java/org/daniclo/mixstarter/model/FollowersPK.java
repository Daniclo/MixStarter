package org.daniclo.mixstarter.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class FollowersPK implements Serializable {
    private long userFollows;
    private long userFollowed;
}

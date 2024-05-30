package org.daniclo.mixstarter.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class UserLikesPK implements Serializable {
    private long userKey;
    private long likedKey;
}
